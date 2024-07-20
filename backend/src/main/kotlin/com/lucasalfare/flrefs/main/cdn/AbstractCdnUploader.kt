package com.lucasalfare.flrefs.main.cdn

import com.lucasalfare.githubwrapper.main.GithubUploadResponseDTO

/**
 * Abstract supertype indicating a general response of a
 * CDN service. All CDN services used should be able to be
 * transformed into this. In other words, all CDN compatible
 * are those that can return a URL to the uploaded resource.
 *
 * Knowing this, their responses can be parsed to this simply.
 */
data class CdnUploadResult(
  val downloadUrl: String
)

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
  ): CdnUploadResult
}