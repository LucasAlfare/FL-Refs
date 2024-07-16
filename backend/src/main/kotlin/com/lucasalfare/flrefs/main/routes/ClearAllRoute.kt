package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.exposed.AppDB
import com.lucasalfare.flrefs.main.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.exposed.ImagesUrls
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.deleteAll

fun Routing.clearAllItemsRoute() {
  // TODO: this MUST to be inside auth block!
  delete("/clear") {
    AppDB.exposedQuery {
      ImagesInfos.deleteAll()
      ImagesUrls.deleteAll()
      // TODO: include call to CDN clear
    }.let {
      when {
        it >= 0 -> {
          return@delete call.respond(HttpStatusCode.OK, "All tables was cleared.")
        }

        else -> {
          return@delete call.respond(HttpStatusCode.InternalServerError, "Nothing was cleared.")
        }
      }
    }
  }
}