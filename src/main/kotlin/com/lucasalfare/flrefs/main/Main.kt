package com.lucasalfare.flrefs.main


import com.lucasalfare.flbase.configureCORS
import com.lucasalfare.flbase.configureRouting
import com.lucasalfare.flbase.configureStatusPages
import com.lucasalfare.flbase.database.initDatabase
import com.lucasalfare.flbase.startWebServer
import com.lucasalfare.flrefs.main.cdn.GithubCdnUploaderAdapter
import com.lucasalfare.flrefs.main.exposed.CreateImageRegistryService
import com.lucasalfare.flrefs.main.exposed.Images
import com.lucasalfare.flrefs.main.routes.uploadRoute
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

val createImageRegistryService: AppServiceAdapter = CreateImageRegistryService
val cdnUploader: AppCdnUploaderAdapter = GithubCdnUploaderAdapter

fun main() {
  initDatabase(Images, dropTablesOnStart = false)

  startWebServer(port = 8080) {
    configureCORS()
    configureStatusPages()

    install(ContentNegotiation) {
      json(Json {
        isLenient = false
        ignoreUnknownKeys = true
      })
    }

    configureRouting {
      uploadRoute()
    }
  }
}