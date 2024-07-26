package com.lucasalfare.flrefs.main.domain.repository

import com.lucasalfare.flrefs.main.domain.model.ImageUrls

interface ImageUrlsRepository {

  suspend fun create(
    relatedImageInfoTitle: String,
    originalUrl: String,
    thumbnailUrl: String
  ): ImageUrls

  suspend fun readByRelatedImageTitle(relatedImageInfoTitle: String): ImageUrls

  suspend fun readAll(
    maxItems: Int = 0,
    offset: Int = 0
  ): List<ImageUrls>

  suspend fun update(
    relatedImageInfoTitle: String,
    newRelatedImageInfoTitle: String? = null,
    newOriginalUrl: String? = null,
    newThumbnailUrl: String? = null
  ): Boolean

  suspend fun delete(relatedImageInfoTitle: String): Boolean
}