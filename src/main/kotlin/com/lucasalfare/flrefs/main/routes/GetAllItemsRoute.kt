package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flbase.BadRequest
import com.lucasalfare.flrefs.main.imagesGetter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.getAllItemsRoute(application: Application) {
  get("/images") {
    val numItems = call.request.queryParameters["num_items"] ?: throw BadRequest("Bad query parameter in request.")
    val page = call.request.queryParameters["page"] ?: throw BadRequest("Bad query parameter in request.")

    application.log.debug("num_items: {}", numItems)
    application.log.debug("page: {}", page)

    return@get imagesGetter.getAll(
      maxItems = numItems.toInt(),
      offset = page.toInt()
    ).let {
      call.respond(HttpStatusCode.OK, it)
    }
  }
}