package com.lucasalfare.flrefs.main.data.exposed.crud

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.Users
import com.lucasalfare.flrefs.main.hashed
import com.lucasalfare.flrefs.main.localization.Message
import com.lucasalfare.flrefs.main.model.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

object UsersCRUD {

  suspend fun create(
    email: String,
    plainPassword: String
  ) = AppDB.exposedQuery {
    try {
      Users.insert {
        it[Users.email] = email
        it[hashedPassword] = plainPassword.hashed()
      }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(Message.DB_USER_INSERTION_ERROR.toString())
    }
  }

  suspend fun readByEmail(email: String) = AppDB.exposedQuery {
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