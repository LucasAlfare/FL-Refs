package com.lucasalfare.flrefs.main

interface AppService {

  suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int>

  suspend fun getAllReferencesInfo(page: Int = 1): AppResult<List<ReferenceInfoItem>>

  suspend fun getReferencesInfoByTerm(term: String, page: Int = 1): AppResult<List<ReferenceInfoItem>>
}

abstract class AbstractAppService : AppService {
  override suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int> {
    TODO("Not yet implemented")
  }

  override suspend fun getAllReferencesInfo(page: Int): AppResult<List<ReferenceInfoItem>> {
    TODO("Not yet implemented")
  }

  override suspend fun getReferencesInfoByTerm(term: String, page: Int): AppResult<List<ReferenceInfoItem>> {
    TODO("Not yet implemented")
  }
}