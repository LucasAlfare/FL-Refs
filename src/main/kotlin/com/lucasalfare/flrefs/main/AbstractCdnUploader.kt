package com.lucasalfare.flrefs.main

import com.lucasalfare.kgasc.UploadResponseDTO

// TODO: create dedicated return type for abstraction
interface CdnUploader {

  suspend fun upload(
    name: String,
    data: ByteArray,
    targetPath: String
  ): UploadResponseDTO
}

abstract class CdnUploaderAdapter : CdnUploader {

  override suspend fun upload(name: String, data: ByteArray, targetPath: String): UploadResponseDTO {
    TODO("Not yet implemented")
  }
}