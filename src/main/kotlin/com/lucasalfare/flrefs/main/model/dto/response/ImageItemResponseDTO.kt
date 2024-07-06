package com.lucasalfare.flrefs.main.model.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ImageItemResponseDTO(
  val name: String,
  val downloadUrl: String
)