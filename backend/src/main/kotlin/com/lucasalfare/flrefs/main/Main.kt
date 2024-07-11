package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.exposed.Images
import com.lucasalfare.flrefs.main.exposed.ImagesGetterExposed
import com.lucasalfare.flrefs.main.exposed.ImagesInserterExposed
import com.lucasalfare.flrefs.main.exposed.initDatabase
import com.lucasalfare.flrefs.main.github.GithubCdnUploader
import com.lucasalfare.flrefs.main.routes.getAllItemsRoute
import com.lucasalfare.flrefs.main.routes.uploadRoute


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
      uploadRoute()
      getAllItemsRoute()
    }
  }
}