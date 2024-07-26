package com.lucasalfare.flrefs.main.infra.data.exposed.repository

import com.lucasalfare.flrefs.main.domain.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.domain.model.ImageUrls
import com.lucasalfare.flrefs.main.domain.repository.ImageUrlsRepository
import com.lucasalfare.flrefs.main.infra.data.exposed.AppDB
import com.lucasalfare.flrefs.main.infra.data.exposed.ImagesUrls
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


object ExposedImagesUrlsRepository : ImageUrlsRepository {

  override suspend fun create(
    relatedImageInfoTitle: String,
    originalUrl: String,
    thumbnailUrl: String
  ) = AppDB.exposedQuery {
    try {
      ImagesUrls.insertReturning {
        it[ImagesUrls.relatedImageInfoTitle] = relatedImageInfoTitle
        it[ImagesUrls.originalUrl] = originalUrl
        it[ImagesUrls.thumbnailUrl] = thumbnailUrl
      }.singleOrNull()!!.let {
        ImageUrls(
          id = it[ImagesUrls.id].value,
          relatedImageInfoTitle = it[ImagesUrls.relatedImageInfoTitle],
          originalUrl = it[ImagesUrls.originalUrl],
          thumbnailUrl = it[ImagesUrls.thumbnailUrl]
        )
      }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(Message.IMAGE_URL_INSERTION_ERROR.toString())
    }
  }

  override suspend fun readByRelatedImageTitle(relatedImageInfoTitle: String) = AppDB.exposedQuery {
    try {
      ImagesUrls.selectAll().where { ImagesUrls.relatedImageInfoTitle eq relatedImageInfoTitle }
        .map { toImageUrl(it) }
        .single()
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(
        Message.IMAGE_URL_SELECTION_ERROR.toString()
      )
    }
  }

  override suspend fun readAll(
    maxItems: Int, offset: Int
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

  override suspend fun update(
    relatedImageInfoTitle: String,
    newRelatedImageInfoTitle: String?,
    newOriginalUrl: String?,
    newThumbnailUrl: String?
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

  override suspend fun delete(relatedImageInfoTitle: String) = AppDB.exposedQuery {
    try {
      ImagesUrls.deleteWhere { ImagesUrls.relatedImageInfoTitle eq relatedImageInfoTitle } > 0
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  private fun toImageUrl(row: ResultRow): ImageUrls =
    ImageUrls(
      id = row[ImagesUrls.id].value,
      relatedImageInfoTitle = row[ImagesUrls.relatedImageInfoTitle],
      originalUrl = row[ImagesUrls.originalUrl],
      thumbnailUrl = row[ImagesUrls.thumbnailUrl]
    )
}