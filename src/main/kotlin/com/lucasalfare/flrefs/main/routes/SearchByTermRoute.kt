package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flbase.BadRequest
import com.lucasalfare.flrefs.main.getByTermHandler
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.searchByTermRoute() {
  get("/by_term") {
    val term = try {
      (call.request.queryParameters["term"])!!
    } catch (e: Exception) {
      throw BadRequest()
    }

    val requestedPage = try {
      (call.request.queryParameters["page"])!!.toInt()
    } catch (e: Exception) {
      throw BadRequest()
    }

    val result = getByTermHandler.getReferencesInfoByTerm(term = term, page = requestedPage)
    return@get call.respond(status = result.statusCode, message = result.data)
  }
}