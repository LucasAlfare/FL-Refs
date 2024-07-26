package com.lucasalfare.flrefs.main.domain.repository

import com.lucasalfare.flrefs.main.domain.model.User

interface UserRepository {

  suspend fun create(
    email: String,
    plainPassword: String
  ): Int

  suspend fun readByEmail(email: String): User?

  // TODO: describe more CRUD functions when needed.
}