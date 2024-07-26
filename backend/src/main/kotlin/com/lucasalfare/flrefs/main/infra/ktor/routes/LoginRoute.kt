package com.lucasalfare.flrefs.main.infra.ktor.routes

import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.domain.model.dto.LoginRequestDTO
import com.lucasalfare.flrefs.main.infra.ktor.security.JwtProvider
import com.lucasalfare.flrefs.main.infra.ktor.security.matchHashed
import com.lucasalfare.flrefs.main.usecase.UserServices
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginRoute() {
  post("/login") {
    val request = call.receive<LoginRequestDTO>()

    UserServices.readByLogin(request)?.let {
      if (!request.plainPassword.matchHashed(it.hashedPassword))
        return@post call.respond(HttpStatusCode.Unauthorized, Message.AUTHENTICATION_ERROR.toString())
      return@post call.respond(HttpStatusCode.OK, JwtProvider.generateJWT(it.email))
    }
    return@post call.respond(HttpStatusCode.Unauthorized, Message.AUTHENTICATION_ERROR.toString())
  }
}