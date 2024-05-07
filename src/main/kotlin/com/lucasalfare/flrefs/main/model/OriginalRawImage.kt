@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model

import kotlinx.serialization.Serializable

// TODO: we must to include the image format info
@Serializable
data class OriginalRawImage(
  val rawImageBytes: ByteArray
)