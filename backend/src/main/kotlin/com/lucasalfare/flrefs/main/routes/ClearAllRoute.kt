package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.deleteAll

/**
 * Defines a route for clearing all items in the database.
 *
 * This route deletes all entries in the ImagesInfos and ImagesUrls tables.
 *
 * TODO: This route should be placed inside an authentication block to restrict access.
 */
fun Routing.clearAllItemsRoute() {
  // TODO: this MUST be inside auth block!
  delete("/clear") {
    AppDB.exposedQuery {
      ImagesInfos.deleteAll()
      ImagesUrls.deleteAll()
      // TODO: include call to CDN clear
    }.let {
      when {
        it >= 0 -> {
          return@delete call.respond(HttpStatusCode.OK, "All tables were cleared.")
        }

        else -> {
          return@delete call.respond(HttpStatusCode.InternalServerError, "Nothing was cleared.")
        }
      }
    }
  }
}