package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.Users
import com.lucasalfare.flrefs.main.localization.Message
import com.lucasalfare.flrefs.main.matchHashed
import com.lucasalfare.flrefs.main.model.User
import com.lucasalfare.flrefs.main.model.dto.LoginRequestDTO
import com.lucasalfare.flrefs.main.security.JwtProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll

fun Route.loginRoute() {
  get("/login") {
    val request = call.receive<LoginRequestDTO>()

    getUserByEmail(request.email).let {
      if (!request.plainPassword.matchHashed(it.hashedPassword))
        return@get call.respond(HttpStatusCode.Unauthorized, Message.AUTHENTICATION_ERROR.toString())

      return@get call.respond(HttpStatusCode.OK, JwtProvider.generateJWT(it.email))
    }
  }
}

private suspend fun getUserByEmail(email: String) = AppDB.exposedQuery {
  try {
    Users.selectAll()
      .where { Users.email eq email }
      .singleOrNull()!!.let {
        User(
          email = it[Users.email],
          hashedPassword = it[Users.hashedPassword]
        )
      }
  } catch (e: Exception) {
    throw UnavailableDatabaseRepository(Message.DB_USER_SELECTION_ERROR.toString())
  }
}