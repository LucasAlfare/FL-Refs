@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model

import kotlinx.serialization.Serializable

@Serializable
data class ReferenceInfoItem(
  val referenceId: Int,
  val title: String,
  val description: String,
  val franchiseName: String,
  val rawThumbnailData: ByteArray
)