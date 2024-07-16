package com.lucasalfare.flrefs.main

import net.coobird.thumbnailator.Thumbnailator.createThumbnail
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Generates a thumbnail image from the provided image bytes.
 *
 * @param imageBytes The byte array containing the image data.
 * @param width The desired width of the thumbnail. Default is 200 pixels.
 * @param height The desired height of the thumbnail. Default is 200 pixels.
 * @return The byte array representing the generated thumbnail image.
 *
 * TODO: implement ".webp" support
 */
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