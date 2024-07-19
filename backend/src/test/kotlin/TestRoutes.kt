import com.lucasalfare.flrefs.main.*
import com.lucasalfare.flrefs.main.cdn.CdnUploader
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.model.dto.ItemResponseDTO
import com.lucasalfare.flrefs.main.model.dto.UploadRequestDTO
import com.lucasalfare.githubwrapper.main.GithubHelper
import com.lucasalfare.githubwrapper.main.GithubUploadResponseDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.test.*


class TestRoutes {

  @BeforeTest
  fun setupDb() {
    initDatabase()
    cdnUploader = FakeCdnGithubUploader
  }

  @AfterTest
  fun dispose() {
    GithubHelper.client.close()
    transaction {
      SchemaUtils.drop(ImagesInfos, ImagesUrls)
    }
  }

  @Test
  fun `test upload POST`() = testApplication {
    val testClient = setup()
    val uploadResult = testClient.post("/uploads") {
      contentType(ContentType.Application.Json)

      val fileName = "test_img.jpg"
      val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

      setBody(
        UploadRequestDTO(
          title = "fake-title",
          description = "fake-description",
          category = "fake-category",
          name = fileName,
          data = bytes
        )
      )
    }
    assertEquals(HttpStatusCode.Created, uploadResult.status)
    assertTrue(uploadResult.body<Int>() > 0)
  }

  @Test
  fun `test general get GET`() = testApplication {
    val testClient = setup()

    testClient.post("/uploads") {
      contentType(ContentType.Application.Json)

      val fileName = "test_img.jpg"
      val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

      setBody(
        UploadRequestDTO(
          title = "fake-title",
          description = "fake-description",
          category = "fake-category",
          name = fileName,
          data = bytes
        )
      )
    }

    val getResult = testClient.get("/images")
    val resultBody = getResult.body<List<ItemResponseDTO>>()
    assertEquals(HttpStatusCode.OK, getResult.status)
    assertTrue(resultBody.isNotEmpty())
  }

  @Test
  fun `test num_items get GET`() = testApplication {
    val testClient = setup()

    repeat(10) {
      testClient.post("/uploads") {
        contentType(ContentType.Application.Json)

        val fileName = "test_img.jpg"
        val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

        setBody(
          UploadRequestDTO(
            title = "fake-title-$it",
            description = "fake-description",
            category = "fake-category",
            name = fileName,
            data = bytes
          )
        )
      }
    }

    val getResult = testClient.get("/images") {
      parameter("num_items", 2)
    }

    val resultBody = getResult.body<List<ItemResponseDTO>>()
    assertEquals(HttpStatusCode.OK, getResult.status)
    assertTrue(resultBody.size == 2)
  }

  @Test
  fun `test offset get GET`() = testApplication {
    val testClient = setup()

    repeat(10) {
      testClient.post("/uploads") {
        contentType(ContentType.Application.Json)

        val fileName = "test_img.jpg"
        val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

        setBody(
          UploadRequestDTO(
            title = "fake-title-$it",
            description = "fake-description",
            category = "fake-category",
            name = fileName,
            data = bytes
          )
        )
      }
    }

    val getResult = testClient.get("/images") {
      parameter("num_items", 2)
      parameter("page", 2)
    }

    val resultBody = getResult.body<List<ItemResponseDTO>>()
    assertEquals(HttpStatusCode.OK, getResult.status)
    assertTrue(resultBody.first().id == 3)
  }

  @Test
  fun `test term get GET`() = testApplication {
    val testClient = setup()

    repeat(2) {
      testClient.post("/uploads") {
        contentType(ContentType.Application.Json)

        val fileName = "test_img.jpg"
        val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

        setBody(
          UploadRequestDTO(
            title = "fake-title-$it",
            description = "fake-description",
            category = "fake-category",
            name = fileName,
            data = bytes
          )
        )
      }
    }

    testClient.post("/uploads") {
      contentType(ContentType.Application.Json)

      val fileName = "test_img.jpg"
      val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

      setBody(
        UploadRequestDTO(
          title = "title-to-be-used-in-url-term-query",
          description = "fake-description",
          category = "fake-category",
          name = fileName,
          data = bytes
        )
      )
    }

    val getResult = testClient.get("/images") {
      parameter("term", "title-to-be-used-in-url-term-query")
    }

    val resultBody = getResult.body<List<ItemResponseDTO>>()
    assertEquals(HttpStatusCode.OK, getResult.status)
    assertTrue(resultBody.size == 1)
  }

  @Test
  fun `test clear DELETE`() = testApplication {
    val testClient = setup()

    testClient.post("/uploads") {
      contentType(ContentType.Application.Json)

      val fileName = "test_img.jpg"
      val bytes = Files.readAllBytes(Path("src/test/resources/$fileName"))

      setBody(
        UploadRequestDTO(
          title = "fake-title",
          description = "fake-description",
          category = "fake-category",
          name = fileName,
          data = bytes
        )
      )
    }

    val deleteResult = testClient.delete("/clear")
    val getResult = testClient.get("/images")
    val resultBody = getResult.body<List<ItemResponseDTO>>()

    assertEquals(HttpStatusCode.OK, deleteResult.status)
    assertTrue(resultBody.isEmpty())
  }
}

private fun ApplicationTestBuilder.setup(): HttpClient {
  application {
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureLargePayloadRejector()
    configureRouting()
  }

  return createClient {
    install(ContentNegotiation) {
      json(Json { isLenient = false })
    }
  }
}

private fun createMockHttpClient(resultName: String): HttpClient {
  val mockEngine = MockEngine { request ->
    // simulates the expected response of the GitHub API
    respond(
      content = """
        {
          "content": {
            "name": "$resultName",
            "path": "uploads/$resultName",
            "download_url": "https://github.com/user/repo/uploads/$resultName"
          }
        }
      """,
      status = HttpStatusCode.Created,
      headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    )
  }

  return HttpClient(mockEngine) {
    install(ContentNegotiation) {
      json()
    }
  }
}

private object FakeCdnGithubUploader : CdnUploader {

  override suspend fun upload(name: String, data: ByteArray, targetPath: String): GithubUploadResponseDTO {
    // Construct target path in the GitHub repository
    val targetPathInRepository = "uploads/$targetPath"

    GithubHelper.client = createMockHttpClient(name)

    // This should trigger to the created MockClient
    val uploadResult = GithubHelper.uploadFileToGithub(
      githubToken = "fake-token",
      username = "fake-user",
      repository = "fake-repo",
      targetPathInRepository = targetPathInRepository,
      inputFileName = name,
      inputFileBytes = data
    )

    // Handle null result from upload
    uploadResult ?: throw UnavailableCdnService("Could not upload to CDN using ${this.javaClass.name}.")

    return uploadResult
  }
}