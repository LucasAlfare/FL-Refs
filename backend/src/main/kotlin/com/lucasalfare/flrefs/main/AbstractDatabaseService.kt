package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.model.ItemResponseDTO

interface AppService {

  suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String,
    concatenation: String
  ): Array<String>

  suspend fun getAll(
    term: String = "",
    maxItems: Int = 0,
    offset: Int = 0
  ): List<ItemResponseDTO>
}

abstract class AppServiceAdapter : com.lucasalfare.flrefs.main.AppService {

  override suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String,
    concatenation: String
  ): Array<String> {
    throw com.lucasalfare.flrefs.main.UnavailableDatabaseService("Not implemented")
  }

  override suspend fun getAll(
    term: String,
    maxItems: Int,
    offset: Int
  ): List<ItemResponseDTO> {
    throw com.lucasalfare.flrefs.main.UnavailableDatabaseService("Not implemented")
  }
}