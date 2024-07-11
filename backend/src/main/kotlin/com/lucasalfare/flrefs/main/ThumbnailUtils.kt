package com.lucasalfare.flrefs.main

import net.coobird.thumbnailator.Thumbnailator.createThumbnail
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

// TODO: implement ".webp" support
fun generateThumbnail(imageBytes: ByteArray, width: Int = 200, height: Int = 200): ByteArray {
  return ByteArrayOutputStream().use { outputStream ->
    createThumbnail(
      ByteArrayInputStream(imageBytes),
      outputStream,
      width,
      height
    )
    outputStream.toByteArray()
  }
}