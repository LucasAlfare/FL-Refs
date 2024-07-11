package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.exposed.Images
import com.lucasalfare.flrefs.main.exposed.ImagesGetterExposed
import com.lucasalfare.flrefs.main.exposed.ImagesInserterExposed
import com.lucasalfare.flrefs.main.exposed.initDatabase
import com.lucasalfare.flrefs.main.github.GithubCdnUploader
import com.lucasalfare.flrefs.main.routes.clearAllItemsRoute
import com.lucasalfare.flrefs.main.routes.getAllItemsRoute
import com.lucasalfare.flrefs.main.routes.uploadItemRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


var imagesInserter: AppService = ImagesInserterExposed
var imagesGetter: AppService = ImagesGetterExposed
var cdnUploader: CdnUploader = GithubCdnUploader

fun main() {
  initDatabase(Images, dropTablesOnStart = false)

  startWebServer(port = 80) {
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureRouting {
      get("/health") {
        call.respondText("We are alive!")
      }

      uploadItemRoute()
      getAllItemsRoute()
      clearAllItemsRoute()
    }
  }
}