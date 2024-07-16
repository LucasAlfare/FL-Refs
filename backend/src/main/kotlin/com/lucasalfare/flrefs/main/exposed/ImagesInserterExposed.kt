package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.UnavailableDatabaseService
import org.jetbrains.exposed.sql.insert

object ImagesInserterExposed {

  suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String,
    concatenation: String
  ) = AppDB.exposedQuery {
    try {
      Images.insert {
        it[Images.title] = title
        it[Images.description] = description
        it[Images.category] = category
        it[Images.name] = name
        it[Images.originalUrl] = originalUrl
        it[Images.thumbnailUrl] = thumbnailUrl
        it[Images.concatenation] = concatenation
      }
      arrayOf(originalUrl, thumbnailUrl)
    } catch (e: Exception) {
      throw UnavailableDatabaseService("Could not to insert image info in database.")
    }
  }
}