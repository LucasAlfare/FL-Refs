package com.lucasalfare.flrefs.main

import com.lucasalfare.flbase.*
import com.lucasalfare.flbase.database.initDatabase
import com.lucasalfare.flrefs.main.cdn.github.GithubCdnUploader
import com.lucasalfare.flrefs.main.data.exposed.Images
import com.lucasalfare.flrefs.main.data.exposed.ImagesInserterExposed
import com.lucasalfare.flrefs.main.routes.uploadRoute


var imagesInserter: AppService = ImagesInserterExposed
var cdnUploader: CdnUploader = GithubCdnUploader

fun main() {
  initDatabase(Images, dropTablesOnStart = false)

  startWebServer(port = 8080) {
    val appRef = this
    configureCORS()
    configureSerialization()
    configureStatusPages()
    configureRouting { uploadRoute(appRef) }
  }
}