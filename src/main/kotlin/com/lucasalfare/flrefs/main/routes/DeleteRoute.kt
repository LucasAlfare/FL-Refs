package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.deleteByIdHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.deleteRoute() {
  delete("/{id}") {
    val id = try {
      call.parameters["id"]!!
    } catch (e: Exception) {
      return@delete call.respond(status = HttpStatusCode.BadRequest, message = "bad URL.")
    }.toInt()

    try {
      deleteByIdHandler.deleteRegistryById(id)
      return@delete call.respond(status = HttpStatusCode.OK, "")
    } catch (e: Exception) {
      return@delete call.respond(status = HttpStatusCode.InternalServerError, message = "error")
    }
  }
}