package com.lucasalfare.flrefs.main.infra.ktor

import com.lucasalfare.flrefs.main.domain.AppError
import com.lucasalfare.flrefs.main.domain.EnvsLoader
import com.lucasalfare.flrefs.main.domain.customRootCause
import com.lucasalfare.flrefs.main.infra.ktor.plugins.LargePayloadRejector
import com.lucasalfare.flrefs.main.infra.ktor.routes.clearAllItemsRoute
import com.lucasalfare.flrefs.main.infra.ktor.routes.getAllItemsRoute
import com.lucasalfare.flrefs.main.infra.ktor.routes.loginRoute
import com.lucasalfare.flrefs.main.infra.ktor.routes.uploadItemRoute
import com.lucasalfare.flrefs.main.infra.ktor.security.JwtProvider
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

/**
 * Configures routing for the application.
 */
internal fun Application.configureRouting() {
  routing {
    loginRoute()
    getAllItemsRoute()
    authenticate("refs-auth-jwt") {
      uploadItemRoute()
      clearAllItemsRoute()
    }

    get("/hello") {
      call.respondText("Hello! We are healthy!! :)")
    }
  }
}

internal fun Application.configureAuthentication() {
  val appRef = this
  install(Authentication) {
    jwt("refs-auth-jwt") {
      realm = EnvsLoader.loadEnv("JWT_AUTH_REALM")

      verifier(JwtProvider.verifier)

      validate { credential ->
        if (credential.payload.getClaim("email").asString() != "") JWTPrincipal(credential.payload)
        else null
      }

      challenge { _, _ ->
        appRef.log.warn(
          "Detected attempt of access a authenticate route with bad JWT Token from ${call.request.origin.remoteHost}"
        )
        call.respond(HttpStatusCode.Unauthorized, "Unable to access the system: Unauthorized!")
      }
    }
  }
}

internal fun Application.configureLargePayloadRejector() {
  install(LargePayloadRejector)
}

/**
 * Configures CORS settings for the application.
 */
internal fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Authorization)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Put)
  }
}

/**
 * Configures content serialization settings for the application.
 */
internal fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false; ignoreUnknownKeys = true })
  }
}

/**
 * Configures error handling and status pages for the application.
 */
internal fun Application.configureStatusPages() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      when (val rootError = cause.customRootCause()) {
        is AppError -> {
          val finalErrorMessage = "${rootError.javaClass.name}: [${rootError.customMessage}]"
          call.application.log.error("$finalErrorMessage. Stacktrace: {}", cause.stackTraceToString())

          call.respond(
//            status = rootError.status,
            message = finalErrorMessage
          )
        }

        else -> {
          val finalErrorMessage = "${cause.javaClass.name}: [${cause.message ?: ""}]"
          call.application.log.error("$finalErrorMessage. Stacktrace: {}", cause.stackTraceToString())

          call.respond(
            status = HttpStatusCode.InternalServerError,
            message = finalErrorMessage
          )
        }
      }
    }
  }
}