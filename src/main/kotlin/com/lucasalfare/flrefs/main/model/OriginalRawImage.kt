@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model

// TODO: we must to include the image format info
data class OriginalRawImage(
  var name: String,
  val rawImageBytes: ByteArray
) {

  init {
    name = name.replace(" ", "_")
  }
}