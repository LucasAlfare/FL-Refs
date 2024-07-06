package com.lucasalfare.flrefs.main.cdn

import com.lucasalfare.flbase.UnavailableDatabaseService
import com.lucasalfare.flrefs.main.AppCdnUploaderAdapter
import com.lucasalfare.flrefs.main.CdnUploadResult
import com.lucasalfare.flrefs.main.InitializationError
import com.lucasalfare.kgasc.GithubHelper
import kotlin.reflect.jvm.jvmName

object GithubCdnUploaderAdapter : AppCdnUploaderAdapter() {

  private const val TARGET_PATH_IN_REPOSITORY = "uploads"
  private val adapterName = GithubHelper::class.jvmName

  private var githubToken =
    System.getenv("GITHUB_API_TOKEN") ?: throw InitializationError("Error initializing $adapterName")

  private var username =
    System.getenv("GITHUB_CDN_USERNAME") ?: throw InitializationError("Error initializing $adapterName")

  private var repository =
    System.getenv("GITHUB_CDN_REPOSITORY") ?: throw InitializationError("Error initializing $adapterName")

  override suspend fun upload(fileName: String, fileBytes: ByteArray): CdnUploadResult {
    val cdnResult = GithubHelper.uploadFileToGithub(
      githubToken = githubToken,
      username = username,
      repository = repository,
      inputFileName = fileName,
      inputFileBytes = fileBytes,
      targetPathInRepository = TARGET_PATH_IN_REPOSITORY
    )

    if (cdnResult != null) {
      return CdnUploadResult(directFileAccessUrl = cdnResult.content.downloadUrl)
    }

    throw UnavailableDatabaseService(
      customMessage = "Could not to upload data using $adapterName"
    )
  }
}