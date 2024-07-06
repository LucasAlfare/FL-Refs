package com.lucasalfare.flrefs.main

import kotlinx.serialization.Serializable

@Serializable
data class CdnUploadResult(val directFileAccessUrl: String)

interface CdnUploader {

  suspend fun upload(fileName: String, fileBytes: ByteArray): CdnUploadResult
}

abstract class AppCdnUploaderAdapter : CdnUploader {

  override suspend fun upload(fileName: String, fileBytes: ByteArray): CdnUploadResult {
    TODO("Not yet implemented")
  }
}