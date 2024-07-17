package com.lucasalfare.flrefs.main.data.exposed.crud

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

data class ImageUrl(
  val id: Int,
  val relatedImageInfoTitle: String,
  val originalUrl: String,
  val thumbnailUrl: String
)

object ImagesUrlsCRUD {

  suspend fun create(
    relatedImageInfoTitle: String,
    originalUrl: String,
    thumbnailUrl: String
  ) = AppDB.exposedQuery {
    try {
      ImagesUrls.insert {
        it[ImagesUrls.relatedImageInfoTitle] = relatedImageInfoTitle
        it[ImagesUrls.originalUrl] = originalUrl
        it[ImagesUrls.thumbnailUrl] = thumbnailUrl
      }.resultedValues!!.singleOrNull()!!.let {
        ImageUrl(
          id = it[ImagesUrls.id].value,
          relatedImageInfoTitle = it[ImagesUrls.relatedImageInfoTitle],
          originalUrl = it[ImagesUrls.originalUrl],
          thumbnailUrl = it[ImagesUrls.thumbnailUrl]
        )
      }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  suspend fun read(relatedImageInfoTitle: String) = AppDB.exposedQuery {
    try {
      ImagesUrls.selectAll().where { ImagesUrls.relatedImageInfoTitle eq relatedImageInfoTitle }
        .mapNotNull { toImageUrl(it) }
        .singleOrNull()
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  suspend fun readAll(
    maxItems: Int = 0,
    offset: Int = 0
  ) = AppDB.exposedQuery {
    try {
      var currentCount: Long
      ImagesUrls
        .selectAll()
        .apply { currentCount = count() }
        .limit(n = if (maxItems == 0) currentCount.toInt() else maxItems, offset.toLong())
        .map { toImageUrl(it) }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  suspend fun update(
    relatedImageInfoTitle: String,
    newRelatedImageInfoTitle: String? = null,
    newOriginalUrl: String? = null,
    newThumbnailUrl: String? = null
  ) = AppDB.exposedQuery {
    try {
      ImagesUrls.update({ ImagesUrls.relatedImageInfoTitle eq relatedImageInfoTitle }) {
        if (newRelatedImageInfoTitle != null) it[ImagesUrls.relatedImageInfoTitle] = newRelatedImageInfoTitle
        if (newOriginalUrl != null) it[originalUrl] = newOriginalUrl
        if (newThumbnailUrl != null) it[thumbnailUrl] = newThumbnailUrl
      } > 0
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  suspend fun delete(relatedImageInfoTitle: String) = AppDB.exposedQuery {
    try {
      ImagesUrls.deleteWhere { ImagesUrls.relatedImageInfoTitle eq relatedImageInfoTitle } > 0
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  private fun toImageUrl(row: ResultRow): ImageUrl =
    ImageUrl(
      id = row[ImagesUrls.id].value,
      relatedImageInfoTitle = row[ImagesUrls.relatedImageInfoTitle],
      originalUrl = row[ImagesUrls.originalUrl],
      thumbnailUrl = row[ImagesUrls.thumbnailUrl]
    )
}