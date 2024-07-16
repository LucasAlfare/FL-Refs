package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import org.jetbrains.exposed.sql.insertAndGetId

object ImagesUrlsRepository {

  suspend fun createImageUrls(
    relatedImageInfoTitle: String,
    originalUrl: String,
    thumbnailUrl: String
  ) = AppDB.exposedQuery {
    try {
      ImagesUrls.insertAndGetId {
        it[ImagesUrls.relatedImageInfoTitle] = relatedImageInfoTitle
        it[ImagesUrls.originalUrl] = originalUrl
        it[ImagesUrls.thumbnailUrl] = thumbnailUrl
      }.value
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository("Can not to create images URLs in the database")
    }
  }
}