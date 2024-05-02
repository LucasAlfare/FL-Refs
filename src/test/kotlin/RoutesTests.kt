import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoutesTests {

  init {
    initDatabase()
  }

  @BeforeTest
  fun setupDb() {
    transaction(AppDB.DB) {
      SchemaUtils.createMissingTablesAndColumns(
        Franchises,
        References,
        ImagesData
      )
    }
  }

  @Test
  fun dispose() {
    transaction(AppDB.DB) {
      SchemaUtils.drop(
        Franchises,
        References,
        ImagesData
      )
    }
  }

  @Test
  fun `test upload`() = testApplication {
    application {
      configureSerialization()
      configureRouting()
    }

    val c = createClient {
      install(ContentNegotiation) {
        json(Json { isLenient = false })
      }
    }

    val result = c.post("/upload") {
      val dto = UploadRequestDTO(
        title = "test2",
        description = "this is a test",
        relatedFranchiseName = "One Piece",
        rawReferenceData = File("img.jpg").readBytes()
      )

      contentType(ContentType.Application.Json)
      setBody(dto)
    }

    assertEquals(1, result.body<Int>())
    assertEquals(HttpStatusCode.Created, result.status)
  }

  @Test
  fun `test get all`() = testApplication {
    application {
      configureSerialization()
      configureRouting()
    }

    val c = createClient {
      install(ContentNegotiation) {
        json(Json { isLenient = false })
      }
    }

    repeat(50) {
      c.post("/upload") {
        val dto = UploadRequestDTO(
          title = "test-num-${Random.nextInt()}",
          description = "this is a test",
          relatedFranchiseName = "One Piece",
          rawReferenceData = File("img.jpg").readBytes()
        )

        contentType(ContentType.Application.Json)
        setBody(dto)
      }
    }

    val requestResult = c.get("/page=3")
    assertEquals(HttpStatusCode.OK, requestResult.status)

    val items = requestResult.body<List<ReferenceItem>>()
    items.map { "ReferenceItem(id=${it.referenceId})" }.forEach { println(it) }

    assertTrue(items.size == 10)
  }
}