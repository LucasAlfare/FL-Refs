package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.data.exposed.crud.UsersCRUD
import com.lucasalfare.flrefs.main.localization.Message
import com.lucasalfare.flrefs.main.matchHashed
import com.lucasalfare.flrefs.main.model.dto.LoginRequestDTO
import com.lucasalfare.flrefs.main.security.JwtProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginRoute() {
  post("/login") {
    val request = call.receive<LoginRequestDTO>()

    UsersCRUD.readByEmail(request.email)?.let {
      if (!request.plainPassword.matchHashed(it.hashedPassword))
        return@post call.respond(HttpStatusCode.Unauthorized, Message.AUTHENTICATION_ERROR.toString())
      return@post call.respond(HttpStatusCode.OK, JwtProvider.generateJWT(it.email))
    }
    return@post call.respond(HttpStatusCode.Unauthorized, Message.AUTHENTICATION_ERROR.toString())
  }
}