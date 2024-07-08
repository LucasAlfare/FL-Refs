package com.lucasalfare.flrefs.main.data.exposed

import com.lucasalfare.flbase.UnavailableDatabaseService
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import org.jetbrains.exposed.sql.insert

object ImagesInserterExposed : AppServiceAdapter() {

  override suspend fun doInsert(
    title: String,
    description: String,
    category: String,
    name: String,
    originalUrl: String,
    thumbnailUrl: String
  ) = AppDB.exposedQuery {
    try {
      Images.insert {
        it[Images.title] = title
        it[Images.description] = description
        it[Images.category] = category
        it[Images.name] = name
        it[Images.originalUrl] = originalUrl
        it[Images.thumbnailUrl] = thumbnailUrl
      }
      arrayOf(originalUrl, thumbnailUrl)
    } catch (e: Exception) {
      throw UnavailableDatabaseService("Could not to insert image info in database.")
    }
  }
}