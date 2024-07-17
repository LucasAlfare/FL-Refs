package com.lucasalfare.flrefs.main.data.exposed.service

import com.lucasalfare.flrefs.main.data.exposed.crud.ImagesUrlsCRUD

object ImagesUrlsService {

  suspend fun createImageUrls(
    relatedImageInfoTitle: String,
    originalUrl: String,
    thumbnailUrl: String
  ) = ImagesUrlsCRUD.create(
    relatedImageInfoTitle = relatedImageInfoTitle,
    originalUrl = originalUrl,
    thumbnailUrl = thumbnailUrl
  ).id
}