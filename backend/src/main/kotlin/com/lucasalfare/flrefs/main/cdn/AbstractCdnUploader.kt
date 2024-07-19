package com.lucasalfare.flrefs.main.cdn

import com.lucasalfare.githubwrapper.main.GithubUploadResponseDTO

/**
 * Interface defining operations for uploading files to a Content Delivery Network (CDN).
 */
interface CdnUploader {

  /**
   * Uploads data to the CDN at the specified target path.
   *
   * @param name The name of the file being uploaded.
   * @param data The byte array representing the file data.
   * @param targetPath The target path on the CDN where the file should be uploaded.
   * @return An instance of [GithubUploadResponseDTO] containing information about the upload operation.
   */
  suspend fun upload(
    name: String,
    data: ByteArray,
    targetPath: String
  ): GithubUploadResponseDTO // <-- should be super type, not specialized
}