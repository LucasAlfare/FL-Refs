package com.lucasalfare.flrefs.main

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun generateThumbnail(imageBytes: ByteArray, width: Int = 200, height: Int = 200): ByteArray {
  val originalImage = ImageIO.read(ByteArrayInputStream(imageBytes))
  val resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)
  val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

  bufferedImage.createGraphics().apply {
    drawImage(resizedImage, 0, 0, null)
    dispose()
  }

  return ByteArrayOutputStream().use { outputStream ->
    ImageIO.write(bufferedImage, "jpg", outputStream)
    outputStream.toByteArray()
  }
}