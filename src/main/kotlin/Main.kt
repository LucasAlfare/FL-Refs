import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun main() {
  initDatabase()

  embeddedServer(Netty, port = 9999) {
    configureSerialization()
    configureRouting()
  }.start(true)
}

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

fun Application.configureRouting() {
  routing {

  }
}

fun initDatabase() {
  AppDB.initialize(
    jdbcUrl = SQLITE_URL,
    jdbcDriverClassName = SQLITE_DRIVER,
    username = "",
    password = ""
  ) {
    // TODO: init tables here
  }
}

// aux auto-gen
private fun generateThumbnail(rawImageBytes: ByteArray, width: Int, height: Int): ByteArray {
  val originalImage: BufferedImage = ImageIO.read(ByteArrayInputStream(rawImageBytes))
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