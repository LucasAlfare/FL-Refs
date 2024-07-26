package com.lucasalfare.flrefs.main.usecase

import com.lucasalfare.flrefs.main.domain.model.dto.LoginRequestDTO
import com.lucasalfare.flrefs.main.domain.repository.UserRepository

object UserServices {

  lateinit var userRepository: UserRepository

  suspend fun create(email: String, plainPassword: String) =
    userRepository.create(email, plainPassword)

  suspend fun readByLogin(loginRequestDTO: LoginRequestDTO) =
    userRepository.readByEmail(loginRequestDTO.email)
}