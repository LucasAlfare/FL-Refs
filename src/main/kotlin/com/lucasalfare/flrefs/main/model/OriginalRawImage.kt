@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model

import kotlinx.serialization.Serializable

@Serializable
data class OriginalRawImage(
  val rawImageBytes: ByteArray
)