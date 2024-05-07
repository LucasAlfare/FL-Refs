package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.exposed.ExposedGetByTermHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.searchByTermRoute() {
  get("/by_term") {
    val term = try {
      (call.request.queryParameters["term"])!!
    } catch (e: Exception) {
      return@get call.respond(HttpStatusCode.BadRequest, "bad URL query parameters.")
    }

    val requestedPage = try {
      (call.request.queryParameters["page"])!!.toInt()
    } catch (e: Exception) {
      return@get call.respond(HttpStatusCode.BadRequest, "bad URL query parameters.")
    }

    return@get try {
      val result = ExposedGetByTermHandler.getReferencesInfoByTerm(term = term, page = requestedPage)
      call.respond(status = result.statusCode, message = result.data)
    } catch (e: AppException) {
      e.printStackTrace()
      call.respond(status = e.statusCode, message = e.customMessage)
    }
  }
}