package com.lucasalfare.flrefs.main.cdn.github

import com.lucasalfare.flbase.AppError
import com.lucasalfare.flrefs.main.CdnUploaderAdapter
import com.lucasalfare.flrefs.main.UnavailableCdnService
import com.lucasalfare.kgasc.GithubHelper

@Suppress("MayBeConstant")
object GithubCdnUploader : CdnUploaderAdapter() {

  private const val ROOT_REPOSITORY = "uploads"

  private val token = System.getenv("GITHUB_API_TOKEN") ?: throw AppError("init error")

  // TODO: retrieve both below from ENV
  private val username = "LucasAlfare"
  private val repository = "cdn-test"

  override suspend fun upload(
    name: String,
    data: ByteArray,
    targetPath: String
  ) = GithubHelper.uploadFileToGithub(
    githubToken = token,
    username = username,
    repository = repository,
    targetPathInRepository = "$ROOT_REPOSITORY/$targetPath",
    inputFileName = name,
    inputFileBytes = data
  ).let {
    if (it == null) throw UnavailableCdnService()
    return@let it
  }
}