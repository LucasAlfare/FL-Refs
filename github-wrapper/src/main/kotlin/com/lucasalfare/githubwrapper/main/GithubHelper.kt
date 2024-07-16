package com.lucasalfare.githubwrapper.main

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@Serializable
private data class Committer(val name: String, val email: String)

@Serializable
private data class GithubUploadRequestDTO(
  val message: String,
  val committer: Committer,
  val content: String
)

@Serializable
data class GithubUploadResponseContent(
  val name: String,
  val path: String,
  @SerialName("download_url") val downloadUrl: String
)

@Serializable
data class GithubUploadResponseDTO(
  val content: GithubUploadResponseContent
)

object GithubHelper {

  internal lateinit var client: HttpClient

  suspend fun uploadFileToGithub(
    githubToken: String,
    username: String,
    repository: String,
    inputFilePath: String,
    targetPathInRepository: String, // omits file name, will be the same of input file
    commitMessage: String = "Upload file via my custom API wrapper 🛠"
  ): GithubUploadResponseDTO? {
    val tmpFile = File(inputFilePath)

    return uploadFileToGithub(
      githubToken = githubToken,
      username = username,
      repository = repository,
      inputFileName = tmpFile.name,
      inputFileBytes = tmpFile.readBytes(),
      targetPathInRepository = targetPathInRepository,
      commitMessage = commitMessage
    )
  }

  @OptIn(ExperimentalEncodingApi::class)
  suspend fun uploadFileToGithub(
    githubToken: String,
    username: String,
    repository: String,
    inputFileName: String,
    inputFileBytes: ByteArray,
    targetPathInRepository: String, // omits file name, will be the same of input file
    commitMessage: String = "Upload file via my custom API wrapper 🛠"
  ): GithubUploadResponseDTO? {
    initClient()

    val fileContentBase64 = Base64.encode(inputFileBytes)
    val finalTargetPath = "$targetPathInRepository/$inputFileName"

    val response = client.put(
      urlString = "https://api.github.com/repos/$username/$repository/contents/$finalTargetPath"
    ) {
      header(HttpHeaders.Authorization, "Bearer $githubToken")
      header(HttpHeaders.Accept, "application/vnd.github+json")
      header(key = "X-GitHub-Api-Version", value = "2022-11-28")
      contentType(ContentType.Application.Json)
      setBody(
        GithubUploadRequestDTO(
          message = commitMessage,
          committer = Committer(
            name = "kGit Helper",
            email = "souluquinha@hotmail.com"
          ),
          content = fileContentBase64,
        )
      )
    }

    println("[GithubHelper] Response of github uploading: $response")

    val result = if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
      response.body<GithubUploadResponseDTO>()
    } else {
      null
    }

    return result.also {
      client.close()
    }
  }

  private fun initClient() {
    if (!GithubHelper::client.isInitialized || !client.isActive) {
      client = HttpClient(CIO) {
        install(ContentNegotiation) {
          json(
            Json {
              isLenient = false
              prettyPrint = true
              ignoreUnknownKeys = true
            }
          )
        }
      }
    }
  }
}