package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.imagesGetter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.getAllItemsRoute(application: Application) {
  get("/images") {
    val numItems = call.request.queryParameters["num_items"] ?: "0"
    val page = call.request.queryParameters["page"] ?: "0"
    val term = call.request.queryParameters["term"] ?: ""

    application.log.debug("num_items: {}", numItems)
    application.log.debug("page: {}", page)
    application.log.debug("term: {}", term)

    return@get imagesGetter.getAll(
      term = term,
      maxItems = numItems.toInt(),
      offset = page.toInt()
    ).let {
      call.respond(HttpStatusCode.OK, it)
    }
  }
}