@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ReferenceInfoItemDTO(
  val referenceId: Int,
  val title: String,
  val description: String,
  val franchiseName: String,
  val rawThumbnailData: ByteArray
)