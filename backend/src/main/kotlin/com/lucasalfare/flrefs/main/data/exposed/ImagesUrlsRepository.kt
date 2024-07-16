package com.lucasalfare.flrefs.main.data.exposed

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import org.jetbrains.exposed.sql.insertAndGetId

/**
 * Repository object responsible for handling image URLs data operations.
 */
object ImagesUrlsRepository {

  /**
   * Inserts a new image URLs record into the database.
   *
   * @param relatedImageInfoTitle The title of the related image information.
   * @param originalUrl The URL of the original image.
   * @param thumbnailUrl The URL of the thumbnail image.
   * @return The ID of the inserted image URLs record.
   * @throws UnavailableDatabaseRepository if the database is unavailable or an error occurs during the insertion.
   */
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
      throw UnavailableDatabaseRepository("Cannot create images URLs in the database")
    }
  }
}