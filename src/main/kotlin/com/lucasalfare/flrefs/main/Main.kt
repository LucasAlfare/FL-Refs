package com.lucasalfare.flrefs.main

import com.lucasalfare.flbase.*
import com.lucasalfare.flbase.database.initDatabase
import com.lucasalfare.flrefs.main.exposed.*
import com.lucasalfare.flrefs.main.routes.*

val getAllHandler: AppService = ExposedGetAllHandler
val getByTermHandler: AppService = ExposedGetByTermHandler
val uploadHandler: AppService = ExposedUploadHandler
val getInfoByIdHandler: AppService = ExposedGetInfoByIdHandler
val deleteByIdHandler: AppService = ExposedDeleteByIdHandler

fun main() {
  initDatabase(Franchises, ReferencesInfo, ImagesData)
  startWebServer(port = 80) {
    configureSerialization()
    configureCORS()
    configureStatusPages()
    configureStaticHtml(
//      Pair("/home", "pages/home.html"),
      Pair("/upload_reference", "pages/upload.html")
    )
    configureRouting {
      homeListRoute()
      searchByTermRoute()
      downloadRoute()
      uploadRoute()
//      deleteRoute()
    }
  }
}