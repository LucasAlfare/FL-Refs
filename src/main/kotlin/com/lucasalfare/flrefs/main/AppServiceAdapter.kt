package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.model.OriginalRawImage
import com.lucasalfare.flrefs.main.model.ReferenceInfoItem
import com.lucasalfare.flrefs.main.model.dto.UploadRequestDTO

interface AppService {

  suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int>

  suspend fun getAllReferencesInfo(page: Int = 1): AppResult<List<ReferenceInfoItem>>

  suspend fun getReferencesInfoByTerm(term: String, page: Int = 1): AppResult<List<ReferenceInfoItem>>

  suspend fun getOriginalImageById(id: Int): AppResult<OriginalRawImage>

  suspend fun deleteRegistryById(id: Int): AppResult<Unit>
}

abstract class AppServiceAdapter : AppService {
  override suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int> {
    TODO("Abstract function has not implemented. Use concrete implementation instead.")
  }

  override suspend fun getAllReferencesInfo(page: Int): AppResult<List<ReferenceInfoItem>> {
    TODO("Abstract function has not implemented. Use concrete implementation instead.")
  }

  override suspend fun getReferencesInfoByTerm(term: String, page: Int): AppResult<List<ReferenceInfoItem>> {
    TODO("Abstract function has not implemented. Use concrete implementation instead.")
  }

  override suspend fun getOriginalImageById(id: Int): AppResult<OriginalRawImage> {
    TODO("Abstract function has not implemented. Use concrete implementation instead.")
  }

  override suspend fun deleteRegistryById(id: Int): AppResult<Unit> {
    TODO("Abstract function has not implemented. Use concrete implementation instead.")
  }
}