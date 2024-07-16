package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.EnvsLoader.databasePasswordEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databasePoolSizeEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUrlEnv
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUsernameEnv
import com.lucasalfare.flrefs.main.EnvsLoader.driverClassNameEnv
import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.github.GithubCdnUploader
import com.lucasalfare.flrefs.main.routes.clearAllItemsRoute
import com.lucasalfare.flrefs.main.routes.getAllItemsRoute
import com.lucasalfare.flrefs.main.routes.uploadItemRoute
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

/**
 * Variable representing the CDN uploader instance.
 */
var cdnUploader: CdnUploader = GithubCdnUploader

/**
 * Main function to start the application.
 */
fun main() {
  // Initialize database connection and create tables if missing
  AppDB.initialize(
    jdbcUrl = databaseUrlEnv,
    jdbcDriverClassName = driverClassNameEnv,
    username = databaseUsernameEnv,
    password = databasePasswordEnv,
    maximumPoolSize = databasePoolSizeEnv.toInt()
  ) {
    SchemaUtils.createMissingTablesAndColumns(ImagesInfos)
    SchemaUtils.createMissingTablesAndColumns(ImagesUrls)
  }

  // Start embedded server
  embeddedServer(Netty, port = 80) {
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureRouting()
  }.start(wait = true)
}

/**
 * Configures routing for the application.
 */
internal fun Application.configureRouting() {
  routing {
    get("/hello") {
      call.respondText("Hello! We are healthy!! :)")
    }
    uploadItemRoute()
    getAllItemsRoute()
    clearAllItemsRoute()
  }
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
