package com.lucasalfare.flrefs.main.domain.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
  val email: String,
  val plainPassword: String
)