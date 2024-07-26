package com.lucasalfare.githubwrapper.main.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Committer(val name: String, val email: String)

@Serializable
data class GithubUploadRequestDTO(
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