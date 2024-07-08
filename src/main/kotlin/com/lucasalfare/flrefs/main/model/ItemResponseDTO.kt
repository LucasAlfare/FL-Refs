package com.lucasalfare.flrefs.main.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemResponseDTO(
  var title: String,
  var description: String,
  var category: String,
  var name: String,
  var downloadUrl: String,
  var thumbnailDownloadUrl: String
)