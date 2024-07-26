package com.lucasalfare.flrefs.main.infra.ktor.routes

import com.lucasalfare.flrefs.main.domain.BadRequest
import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.usecase.DataServices
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines a route for retrieving all items based on query parameters.
 *
 * This route handles GET requests to fetch images with optional filtering, pagination, and search term.
 */
fun Route.getAllItemsRoute() {
  get("/images") {
    val numItems = call.request.queryParameters["num_items"] ?: "0"
    val page = call.request.queryParameters["page"] ?: "0"
    val term = call.request.queryParameters["term"] ?: ""

    if (numItems.toIntOrNull() == null || page.toIntOrNull() == null) {
      throw BadRequest(Message.BAD_GET_QUERY_PARAMS.toString())
    }

    DataServices.getAllDataAsItemResponseDTO(
      term, numItems.toInt(), page.toInt()
    ).let {
      return@get call.respond(HttpStatusCode.OK, it)
    }
  }
}