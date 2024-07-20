package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.EnvsLoader.databasePasswordEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databasePoolSizeEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUrlEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUsernameEnv
import com.lucasalfare.flrefs.main.EnvsLoader.driverClassNameEnv
import com.lucasalfare.flrefs.main.cdn.CdnUploader
import com.lucasalfare.flrefs.main.cdn.github.GithubCdnUploader
import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.data.exposed.Users
import com.lucasalfare.flrefs.main.data.exposed.crud.UsersCRUD
import com.lucasalfare.flrefs.main.plugins.LargePayloadRejector
import com.lucasalfare.flrefs.main.routes.clearAllItemsRoute
import com.lucasalfare.flrefs.main.routes.getAllItemsRoute
import com.lucasalfare.flrefs.main.routes.loginRoute
import com.lucasalfare.flrefs.main.routes.uploadItemRoute
import com.lucasalfare.flrefs.main.security.JwtProvider
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import java.util.*

/**
 * Variable representing the CDN uploader instance.
 */
lateinit var cdnUploader: CdnUploader

lateinit var currentLocale: Locale

/**
 * Main function to start the application.
 */
suspend fun main() {
  // Initialize database connection and create tables if missing
  initDatabase()
  initOther()
  startServer()
}

internal fun startServer() {
  // Start embedded server
  embeddedServer(Netty, port = 80) {
    configureAuthentication()
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureLargePayloadRejector()
    configureRouting()
  }.start(wait = true)
}

internal suspend fun initOther() {
  runCatching {
    // TODO: get true email and password from ENV
    UsersCRUD.create("", "")
  }

  cdnUploader = GithubCdnUploader
  currentLocale = Locale.ENGLISH
}

fun initDatabase() {
  AppDB.initialize(
    jdbcUrl = databaseUrlEnv,
    jdbcDriverClassName = driverClassNameEnv,
    username = databaseUsernameEnv,
    password = databasePasswordEnv,
    maximumPoolSize = databasePoolSizeEnv.toInt()
  ) {
    SchemaUtils.createMissingTablesAndColumns(
      Users,
      ImagesInfos,
      ImagesUrls
    )
  }
}

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
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Put)
  }
}

/**
 * Configures content serialization settings for the application.
 */
internal fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
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
            status = rootError.status,
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
