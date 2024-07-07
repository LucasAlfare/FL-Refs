package com.lucasalfare.flrefs.main.cdn.github

import com.lucasalfare.flbase.AppError
import com.lucasalfare.flrefs.main.CdnUploaderAdapter
import com.lucasalfare.flrefs.main.UnavailableCdnService
import com.lucasalfare.kgasc.GithubHelper

object GithubCdnUploader : CdnUploaderAdapter() {

  private const val ROOT_DIRECTORY = "uploads"

  private val token = System.getenv("GITHUB_API_TOKEN") ?: throw AppError("init error")
  private val username = System.getenv("GITHUB_CDN_USERNAME") ?: throw AppError("init error")
  private val repository = System.getenv("GITHUB_CDN_REPOSITORY") ?: throw AppError("init error")

  override suspend fun upload(
    name: String,
    data: ByteArray,
    targetPath: String
  ) = GithubHelper.uploadFileToGithub(
    githubToken = token,
    username = username,
    repository = repository,
    targetPathInRepository = "$ROOT_DIRECTORY/$targetPath",
    inputFileName = name,
    inputFileBytes = data
  ).let {
    if (it == null) throw UnavailableCdnService("Could not to upload to CDN using ${this.javaClass.name}.")
    return@let it
  }
}