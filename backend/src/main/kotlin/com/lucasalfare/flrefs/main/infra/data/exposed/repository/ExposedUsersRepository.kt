package com.lucasalfare.flrefs.main.infra.data.exposed.repository

import com.lucasalfare.flrefs.main.domain.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.domain.model.User
import com.lucasalfare.flrefs.main.domain.repository.UserRepository
import com.lucasalfare.flrefs.main.infra.data.exposed.AppDB
import com.lucasalfare.flrefs.main.infra.data.exposed.Users
import com.lucasalfare.flrefs.main.infra.ktor.security.hashed
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

object ExposedUsersRepository : UserRepository {

  override suspend fun create(
    email: String,
    plainPassword: String
  ) = AppDB.exposedQuery {
    try {
      Users.insertAndGetId {
        it[Users.email] = email
        it[hashedPassword] = plainPassword.hashed()
      }.value
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(Message.DB_USER_INSERTION_ERROR.toString())
    }
  }

  override suspend fun readByEmail(email: String) = AppDB.exposedQuery {
    try {
      Users
        .selectAll()
        .where { Users.email eq email }
        .singleOrNull().let {
          if (it != null) {
            User(it[Users.email], it[Users.hashedPassword])
          } else null
        }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(Message.DB_USER_SELECTION_ERROR.toString())
    }
  }
}