package com.lucasalfare.flrefs.main

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class Constants {
  companion object {
    @Deprecated("Postgres URL are now handled by Environment variables.")
    const val POSTGRES_URL = "jdbc:postgresql://TTTT:0000/"

    @Deprecated("Postgres URL are now handled by Environment variables.")
    const val POSTGRES_DRIVER = "org.postgresql.Driver"

    const val SQLITE_URL = "jdbc:sqlite:./data.db"
    const val SQLITE_DRIVER = "org.sqlite.JDBC"
  }
}

object ImageUtil {

  fun generateThumbnail(imageBytes: ByteArray, width: Int = 200, height: Int = 200): ByteArray {
    val originalImage: BufferedImage = ImageIO.read(ByteArrayInputStream(imageBytes))
    val resizedImage: Image = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT)
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = bufferedImage.createGraphics()
    graphics.drawImage(resizedImage, 0, 0, null)
    graphics.dispose()

    val outputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "jpg", outputStream)
    outputStream.close()

    return outputStream.toByteArray()
  }
}