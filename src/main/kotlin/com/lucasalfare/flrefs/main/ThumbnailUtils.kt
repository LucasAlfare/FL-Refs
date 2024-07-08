package com.lucasalfare.flrefs.main

import net.coobird.thumbnailator.Thumbnailator
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun generateThumbnail(imageBytes: ByteArray, width: Int = 200, height: Int = 200): ByteArray {
  return ByteArrayOutputStream().use {
    Thumbnailator.createThumbnail(ByteArrayInputStream(imageBytes), it, width, height)
    it.toByteArray()
  }
}