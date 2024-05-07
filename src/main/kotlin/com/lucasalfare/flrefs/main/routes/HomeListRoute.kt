package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.exposed.ExposedGetAllHandler
import com.lucasalfare.flrefs.main.getAllHandler
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.homeListRoute() {
  get("/") {
    val requestedPage = (call.request.queryParameters["page"] ?: "1").toInt()
    return@get try {
      val result = getAllHandler.getAllReferencesInfo(requestedPage)
      call.respond(status = result.statusCode, message = result.data)
    } catch (e: AppException) {
      e.printStackTrace()
      call.respond(status = e.statusCode, message = e.customMessage)
    }
  }
}