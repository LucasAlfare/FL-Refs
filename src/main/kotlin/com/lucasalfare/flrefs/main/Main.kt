package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.exposed.Franchises
import com.lucasalfare.flrefs.main.exposed.ImagesData
import com.lucasalfare.flrefs.main.exposed.ReferencesInfo
import com.lucasalfare.flrefs.main.routes.homeListRoute
import com.lucasalfare.flrefs.main.routes.searchByTermRoute
import com.lucasalfare.flrefs.main.routes.uploadRoute
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
  initDatabase()

  embeddedServer(Netty, port = 9999) {
    configureSerialization()
    configureCORS()
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

fun Application.configureCORS() {
  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
  }
}

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json { isLenient = false })
  }
}

fun Application.configureRouting() {
  routing {
    uploadRoute()
    homeListRoute()
    searchByTermRoute()
  }
}