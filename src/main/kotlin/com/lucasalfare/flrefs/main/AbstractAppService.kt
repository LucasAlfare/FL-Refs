package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.model.ReferenceInfoItem
import com.lucasalfare.flrefs.main.model.dto.UploadRequestDTO

interface AppService {

  suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int>

  suspend fun getAllReferencesInfo(page: Int = 1): AppResult<List<ReferenceInfoItem>>

  suspend fun getReferencesInfoByTerm(term: String, page: Int = 1): AppResult<List<ReferenceInfoItem>>
}

abstract class AbstractAppService : AppService {
  override suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int> {
    TODO("Abstract function has not implemented. Using concrete implementation instead.")
  }

  override suspend fun getAllReferencesInfo(page: Int): AppResult<List<ReferenceInfoItem>> {
    TODO("Abstract function has not implemented. Using concrete implementation instead.")
  }

  override suspend fun getReferencesInfoByTerm(term: String, page: Int): AppResult<List<ReferenceInfoItem>> {
    TODO("Abstract function has not implemented. Using concrete implementation instead.")
  }
}