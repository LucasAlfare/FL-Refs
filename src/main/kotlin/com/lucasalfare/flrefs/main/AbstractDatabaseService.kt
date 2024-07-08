package com.lucasalfare.flrefs.main

interface AppService {

  suspend fun doInsert(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String
  ): Array<String>
}

abstract class AppServiceAdapter : AppService {

  override suspend fun doInsert(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String
  ): Array<String> {
    TODO("Not yet implemented")
  }
}