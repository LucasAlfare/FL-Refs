package com.lucasalfare.flrefs.main.infra.thumbnail.thumbnailator

import com.lucasalfare.flrefs.main.domain.ThumbnailGenerator
import net.coobird.thumbnailator.Thumbnailator
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ThumbnailatorThumbnailGenerator : ThumbnailGenerator {
  override fun generate(imageBytes: ByteArray, width: Int, height: Int): ByteArray {
    return ByteArrayOutputStream().use { outputStream ->
      Thumbnailator.createThumbnail(
        ByteArrayInputStream(imageBytes),
        outputStream,
        width,
        height
      )
      outputStream.toByteArray()
    }
  }
}