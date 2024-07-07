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
    downloadUrl: String,
    thumbnailDownloadUrl: String
  ) = AppDB.exposedQuery {
    try {
      Images.insert {
        it[Images.title] = title
        it[Images.description] = description
        it[Images.category] = category
        it[Images.name] = name
        it[Images.downloadUrl] = downloadUrl
        it[Images.thumbnailDownloadUrl] = thumbnailDownloadUrl
      }
      arrayOf(downloadUrl, thumbnailDownloadUrl)
    } catch (e: Exception) {
      throw UnavailableDatabaseService("Could not to insert image info in database.")
    }
  }
}