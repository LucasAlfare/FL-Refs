package com.lucasalfare.flrefs.main

import com.lucasalfare.kgasc.main.CdnUploadResponseDTO

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
   * @return An instance of [CdnUploadResponseDTO] containing information about the upload operation.
   */
  suspend fun upload(
    name: String,
    data: ByteArray,
    targetPath: String
  ): CdnUploadResponseDTO
}

/**
 * Abstract class implementing [CdnUploader] interface with default unimplemented methods.
 * Typically used as a base for CDN uploader adapters.
 */
abstract class CdnUploaderAdapter : CdnUploader {

  /**
   * Throws [UnavailableCdnService] indicating that CDN service is not implemented.
   *
   * @throws UnavailableCdnService Always throws this exception since the method is not implemented.
   */
  override suspend fun upload(name: String, data: ByteArray, targetPath: String): CdnUploadResponseDTO {
    throw UnavailableCdnService("Not implemented")
  }
}