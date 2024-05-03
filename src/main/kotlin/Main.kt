@file:Suppress("ArrayInDataClass")

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object Franchises : IntIdTable("Franchises") {
  val name = text("name").uniqueIndex()
}

object References : IntIdTable("References") {
  val title = text("title").uniqueIndex()
  val description = text("description")
  val relatedFranchiseId = integer("related_franchise_id").references(Franchises.id)
  val concatenation = text("concatenation")
}

object ImagesData : IntIdTable("ImagesData") {
  val rawReferenceData = blob("raw_reference_data")
  val rawThumbnailData = blob("raw_thumbnail_data")
  val relatedFranchiseId = integer("related_franchise_id").references(Franchises.id)
  val relatedReferenceId = integer("related_reference_id").references(References.id)
}

@Serializable
data class UploadRequestDTO(
  val title: String,
  val description: String,
  val relatedFranchiseName: String,
  val rawReferenceData: ByteArray
) {

  fun createConcatenation() = buildString {
    append(title)
    append(description)
    append(relatedFranchiseName)
  }
}

@Serializable
data class ReferenceItem(
  val referenceId: Int,
  val title: String,
  val description: String,
  val franchiseName: String,
  val rawThumbnailData: ByteArray
)

fun main() {
  initDatabase()

  embeddedServer(Netty, port = 9999) {
    configureSerialization()
    configureRouting()
  }.start(true)
}

fun initDatabase() {
  AppDB.initialize(
    jdbcUrl = SQLITE_URL,
    jdbcDriverClassName = SQLITE_DRIVER,
    username = "",
    password = ""
  ) {
    transaction(AppDB.DB) {
      SchemaUtils.createMissingTablesAndColumns(
        Franchises,
        References,
        ImagesData
      )
    }
  }
}

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

fun Application.configureRouting() {
  routing {
    post("/upload") {
      val uploadRequestDTO = try {
        call.receive<UploadRequestDTO>()
      } catch (e: Exception) {
        return@post call.respond(HttpStatusCode.BadRequest, "serialization error")
      }

      return@post try {
        AppDB.query {
          // try get/create franchise
          val franchiseId = try {
            Franchises
              .selectAll()
              .where { Franchises.name eq uploadRequestDTO.relatedFranchiseName }
              .singleOrNull()
              .let { result ->
                if (result != null) {
                  result[Franchises.id].value
                } else {
                  Franchises.insertAndGetId { it[name] = uploadRequestDTO.relatedFranchiseName }.value
                }
              }
          } catch (e: Exception) {
            return@query call.respond(
              HttpStatusCode.InternalServerError,
              "error searching/creating requested franchise"
            )
          }

          // try insert reference info
          val referenceId = try {
            References.insertAndGetId {
              it[title] = uploadRequestDTO.title
              it[description] = uploadRequestDTO.description
              it[relatedFranchiseId] = franchiseId
              it[concatenation] = uploadRequestDTO.createConcatenation()
            }.value
          } catch (e: Exception) {
            return@query call.respond(HttpStatusCode.InternalServerError, "error inserting the reference")
          }

          // try to generate thumbnail
          val thumbnailBytes = try {
            generateThumbnail(uploadRequestDTO.rawReferenceData)
          } catch (e: Exception) {
            return@query call.respond(
              HttpStatusCode.InternalServerError,
              "error on creating thumbnail: invalid raw image data"
            )
          }

          // try to insert binary data
          ImagesData.insert {
            it[rawReferenceData] = ExposedBlob(uploadRequestDTO.rawReferenceData)
            it[rawThumbnailData] = ExposedBlob(thumbnailBytes)
            it[relatedFranchiseId] = franchiseId
            it[relatedReferenceId] = referenceId
          }

          // return created
          return@query call.respond(HttpStatusCode.Created, referenceId)
        }
      } catch (e: Exception) {
        return@post call.respond(HttpStatusCode.InternalServerError, "error creating data")
      }
    }

    /*
    post("/upload") {
      val uploadRequestDTO = try {
        call.receive<UploadRequestDTO>()
      } catch (e: Exception) {
        return@post call.respond(HttpStatusCode.BadRequest, "serialization error")
      }
      val franchiseId = try {
        AppDB.query {
          Franchises
            .selectAll()
            .where { Franchises.name eq uploadRequestDTO.relatedFranchiseName }
            .singleOrNull()
            .let { result ->
              if (result != null) {
                result[Franchises.id].value
              } else {
                Franchises.insertAndGetId { it[name] = uploadRequestDTO.relatedFranchiseName }.value
              }
            }
        }
      } catch (e: Exception) {
        return@post call.respond(HttpStatusCode.InternalServerError, "error searching/creating requested franchise")
      }
      val referenceId = try {
        AppDB.query {
          References.insertAndGetId {
            it[title] = uploadRequestDTO.title
            it[description] = uploadRequestDTO.description
            it[relatedFranchiseId] = franchiseId
            it[concatenation] = uploadRequestDTO.createConcatenation()
          }
        }.value
      } catch (e: Exception) {
        return@post call.respond(HttpStatusCode.InternalServerError, "error inserting the reference")
      }
      val thumbnailBytes = try {
        generateThumbnail(uploadRequestDTO.rawReferenceData)
      } catch (e: Exception) {
        return@post call.respond(
          HttpStatusCode.InternalServerError,
          "error on creating thumbnail: invalid raw image data"
        )
      }
      try {
        AppDB.query {
          ImagesData.insert {
            it[rawReferenceData] = ExposedBlob(uploadRequestDTO.rawReferenceData)
            it[rawThumbnailData] = ExposedBlob(thumbnailBytes)
            it[relatedFranchiseId] = franchiseId
            it[relatedReferenceId] = referenceId
          }
        }
        return@post call.respond(HttpStatusCode.Created, referenceId)
      } catch (e: Exception) {
        return@post call.respond(HttpStatusCode.InternalServerError, "error when inserting images data")
      }
    }
     */

    get("/page={requested_page}") {
      val pageSize = 10
      val requestedPage = (call.parameters["requested_page"])?.toLong() ?: 1L
      val searchOffset = (requestedPage - 1) * pageSize

      val items = AppDB.query {
        (Franchises leftJoin References leftJoin ImagesData)
          .selectAll()
          .orderBy(References.id to SortOrder.ASC)
          .limit(n = pageSize, offset = maxOf(0, searchOffset - 1))
          .map {
            ReferenceItem(
              referenceId = it[References.id].value,
              title = it[References.title],
              description = it[References.description],
              franchiseName = it[Franchises.name],
              rawThumbnailData = it[ImagesData.rawThumbnailData].bytes
            )
          }
      }

      return@get call.respond(HttpStatusCode.OK, items)
    }

    // TODO: exactly equals to general GET, but should be cashed
    get("/term={term}") {
      return@get call.respond(HttpStatusCode.NotImplemented, "search by term not implemented yet.")
    }
  }
}

// aux auto-gen
private fun generateThumbnail(
  rawImageBytes: ByteArray,
  width: Int = 200,
  height: Int = 200
): ByteArray {
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