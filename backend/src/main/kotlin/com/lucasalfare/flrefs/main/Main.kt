package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.EnvsLoader.databasePasswordEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databasePoolSizeEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUrlEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUsernameEnv
import com.lucasalfare.flrefs.main.EnvsLoader.driverClassNameEnv
import com.lucasalfare.flrefs.main.exposed.AppDB
import com.lucasalfare.flrefs.main.exposed.Images
import com.lucasalfare.flrefs.main.github.GithubCdnUploader
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils

var cdnUploader: CdnUploader = GithubCdnUploader

fun main() {
  AppDB.initialize(
    jdbcUrl = databaseUrlEnv,
    jdbcDriverClassName = driverClassNameEnv,
    username = databaseUsernameEnv,
    password = databasePasswordEnv,
    maximumPoolSize = databasePoolSizeEnv.toInt()
  ) {
    SchemaUtils.createMissingTablesAndColumns(Images)
  }

  embeddedServer(Netty, port = 80) {
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureRouting()
  }.start(wait = true)
}

internal fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Put)
  }
}

internal fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

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

internal fun Application.configureRouting() {
  routing {
    //
  }
}