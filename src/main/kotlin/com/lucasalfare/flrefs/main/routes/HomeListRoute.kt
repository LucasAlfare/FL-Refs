package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.getAllHandler
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.homeListRoute() {
  get("/") {
    val requestedPage = (call.request.queryParameters["page"] ?: "1").toInt()
    val result = getAllHandler.getAllReferencesInfo(requestedPage)
    return@get call.respond(status = result.statusCode, message = result.data)
  }
}