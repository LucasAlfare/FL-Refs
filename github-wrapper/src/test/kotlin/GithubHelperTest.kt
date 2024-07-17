import com.lucasalfare.githubwrapper.main.GithubHelper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubHelperTest {

  @Test
  fun testUploadFileToGithub() = runBlocking {
    // We use our mock client as our wrapper client
    GithubHelper.client = createMockHttpClient()

    // Tries to call the testing function
    val response = GithubHelper.uploadFileToGithub(
      githubToken = "fake-token",
      username = "user",
      repository = "repo",
      inputFileName = "test.txt",
      inputFileBytes = "test content".toByteArray(),
      targetPathInRepository = "uploads"
    )

    // Check the results
    assertEquals("test.txt", response?.content?.name)
    assertEquals("uploads/test.txt", response?.content?.path)
    assertEquals("https://github.com/user/repo/uploads/test.txt", response?.content?.downloadUrl)
  }
}

private fun createMockHttpClient(): HttpClient {
  val mockEngine = MockEngine { request ->
    // simulates the "real" response of the GitHub API
    respond(
      content = """
        {
          "content": {
            "name": "test.txt",
            "path": "uploads/test.txt",
            "download_url": "https://github.com/user/repo/uploads/test.txt"
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