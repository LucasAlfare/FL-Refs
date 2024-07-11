package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.BadRequest
import com.lucasalfare.flrefs.main.imagesGetter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.getAllItemsRoute() {
  get("/images") {
    val numItems = call.request.queryParameters["num_items"] ?: "0"
    val page = call.request.queryParameters["page"] ?: "0"
    val term = call.request.queryParameters["term"] ?: ""

    if (numItems.toIntOrNull() == null || page.toIntOrNull() == null) {
      throw BadRequest("Bad GET query parameters")
    }

    return@get imagesGetter.getAll(
      term = term,
      maxItems = numItems.toInt(),
      offset = page.toInt()
    ).let {
      call.respond(HttpStatusCode.OK, it)
    }
  }
}