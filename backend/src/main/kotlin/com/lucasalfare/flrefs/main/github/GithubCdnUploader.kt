package com.lucasalfare.flrefs.main.github

import com.lucasalfare.flrefs.main.CdnUploaderAdapter
import com.lucasalfare.flrefs.main.EnvsLoader.loadEnv
import com.lucasalfare.flrefs.main.UnavailableCdnService
import com.lucasalfare.kgasc.main.GithubHelper
import com.lucasalfare.kgasc.main.UploadResponseDTO

object GithubCdnUploader : CdnUploaderAdapter() {

  private const val ROOT_DIRECTORY = "uploads"

  private val token = loadEnv("GITHUB_API_TOKEN")
  private val username = loadEnv("GITHUB_CDN_USERNAME")
  private val repository = loadEnv("GITHUB_CDN_REPOSITORY")

  override suspend fun upload(
    name: String,
    data: ByteArray,
    targetPath: String
  ): UploadResponseDTO = GithubHelper.uploadFileToGithub(
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