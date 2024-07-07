package com.lucasalfare.flrefs.main

interface AppService {

  suspend fun doInsert(
    title: String,
    description: String,
    category: String,
    name: String,
    downloadUrl: String,
    thumbnailDownloadUrl: String
  ): Array<String>
}

abstract class AppServiceAdapter : AppService {

  override suspend fun doInsert(
    title: String,
    description: String,
    category: String,
    name: String,
    downloadUrl: String,
    thumbnailDownloadUrl: String
  ): Array<String> {
    TODO("Not yet implemented")
  }
}