package com.lucasalfare.flrefs.main.infra.cdn.github

import com.lucasalfare.flrefs.main.domain.CdnUploader
import com.lucasalfare.flrefs.main.domain.EnvsLoader.loadEnv
import com.lucasalfare.flrefs.main.domain.UnavailableCdnService
import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.domain.model.CdnUploadResult
import com.lucasalfare.githubwrapper.main.domain.GithubUploadResponseDTO
import com.lucasalfare.githubwrapper.main.infra.GithubHelper

/**
 * GitHub CDN uploader implementation using GitHub API for file uploads.
 */
object GithubCdnUploader : CdnUploader {

  private const val ROOT_DIRECTORY = "uploads"

  // Load GitHub API token, username, and repository from environment variables
  private val token = loadEnv("GITHUB_API_TOKEN")
  private val username = loadEnv("GITHUB_CDN_USERNAME")
  private val repository = loadEnv("GITHUB_CDN_REPOSITORY")

  /**
   * Uploads a file to GitHub CDN.
   *
   * @param name The name of the file to be uploaded.
   * @param data The byte array containing the file data.
   * @param targetPath The target path on the CDN where the file should be uploaded.
   * @return An instance of [GithubUploadResponseDTO] containing information about the upload operation.
   * @throws UnavailableCdnService If the upload operation fails or the CDN service is unavailable.
   */
  override suspend fun upload(name: String, data: ByteArray, targetPath: String): CdnUploadResult {
    // Construct target path in the GitHub repository´
    val targetPathInRepository = "$ROOT_DIRECTORY/$targetPath"

    // Upload file using GitHub API
    val uploadResult = GithubHelper.uploadFileToGithub(
      githubToken = token,
      username = username,
      repository = repository,
      targetPathInRepository = targetPathInRepository,
      inputFileName = name,
      inputFileBytes = data
    )

    // Handle null result from upload
    uploadResult ?: throw UnavailableCdnService(Message.CDN_UPLOAD_ERROR.format(this.javaClass.name))

    return uploadResult.toCdnUploadResult()
  }

  private fun GithubUploadResponseDTO.toCdnUploadResult() =
    CdnUploadResult(downloadUrl = content.downloadUrl)
}