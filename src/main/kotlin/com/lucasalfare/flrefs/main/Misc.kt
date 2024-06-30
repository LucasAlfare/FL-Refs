package com.lucasalfare.flrefs.main

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ImageUtil {

  companion object {
    fun generateThumbnail(imageBytes: ByteArray, width: Int = 200, height: Int = 200): ByteArray {
      val originalImage: BufferedImage = ImageIO.read(ByteArrayInputStream(imageBytes))
      val resizedImage: Image = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT)
      val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
      val graphics = bufferedImage.createGraphics()
      graphics.drawImage(resizedImage, 0, 0, null)
      graphics.dispose()

      val outputStream = ByteArrayOutputStream()
      // we use jpg format to really compress the image
      ImageIO.write(bufferedImage, "jpg", outputStream)
      outputStream.close()

      return outputStream.toByteArray()
    }
  }
}