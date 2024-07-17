import com.lucasalfare.flrefs.main.*
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.model.UploadRequestDTO
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
  fun testUpload() = testApplication {
    application {
      configureCORS()
      configureSerialization()
      configureStatusPages()
      configureLargePayloadRejector()
      configureRouting()
    }
    val testClient = createClient {
      install(ContentNegotiation) {
        json(Json { isLenient = false })
      }
    }
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

private object FakeCdnGithubUploader : CdnUploaderAdapter() {

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