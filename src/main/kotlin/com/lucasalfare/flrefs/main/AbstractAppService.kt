package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.model.dto.response.ImageItemResponseDTO

interface AppService {

  suspend fun createImageRegistry(name: String, downloadUrl: String)

  suspend fun getAllItems(): List<ImageItemResponseDTO>
}

abstract class AppServiceAdapter : AppService {
  override suspend fun createImageRegistry(name: String, downloadUrl: String) {
    TODO("Not yet implemented")
  }

  override suspend fun getAllItems(): List<ImageItemResponseDTO> {
    TODO("Not yet implemented")
  }
}