package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.exposed.*
import com.lucasalfare.flrefs.main.routes.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

val getAllHandler: AppService = ExposedGetAllHandler
val getByTermHandler: AppService = ExposedGetByTermHandler
val uploadHandler: AppService = ExposedUploadHandler
val getInfoByIdHandler: AppService = ExposedGetInfoByIdHandler
val deleteByIdHandler: AppService = ExposedDeleteByIdHandler

fun main() {
  initDatabase()

  embeddedServer(Netty, port = 80) {
    configureSerialization()
    configureCORS()
    configureStatusPages()
    configureStaticHtml()
    configureRouting()
  }.start(true)
}

fun initDatabase(
  dropTablesOnStart: Boolean = false
) {
  AppDB.initialize(
    jdbcUrl = System.getenv("DB_JDBC_URL") ?: Constants.SQLITE_URL,
    jdbcDriverClassName = System.getenv("DB_JDBC_DRIVER") ?: Constants.SQLITE_DRIVER,
    username = System.getenv("DB_USERNAME") ?: "",
    password = System.getenv("DB_PASSWORD") ?: ""
  ) {
    if (dropTablesOnStart) {
      SchemaUtils.drop(
        Franchises,
        ReferencesInfo,
        ImagesData
      )
    }

    transaction(AppDB.DB) {
      SchemaUtils.createMissingTablesAndColumns(
        Franchises,
        ReferencesInfo,
        ImagesData
      )
    }
  }
}

fun Application.configureStatusPages() {
  install(StatusPages) {
    exception<AppError> { call, cause ->
      when (cause) {
        is UnavailableDatabaseService -> call.respond(cause.status, cause.customMessage ?: "UnavailableDatabaseService")
        is BadRequest -> call.respond(cause.status, "BadRequest")
        is SerializationError -> call.respond(cause.status, "SerializationError")
        is ValidationError -> call.respond(cause.status, "ValidationError")
        else -> call.respond(cause.status, "InternalServerError")
      }
    }
  }
}

fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowMethod(HttpMethod.Delete)
  }
}

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

fun Application.configureStaticHtml() {
  routing {
    staticResources(remotePath = "/inicio", basePackage = "assets", index = "pages/home.html")
    staticResources(remotePath = "/fazer_upload", basePackage = "assets", index = "pages/upload.html")
  }
}

fun Application.configureRouting() {
  routing {
    uploadRoute()
    homeListRoute()
    searchByTermRoute()
    downloadRoute()
    deleteRoute()
  }
}
