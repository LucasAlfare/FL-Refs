package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.getReferenceInfoItemByIdHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.downloadRoute() {
  get("/download/{info_id}") {
    val referenceInfoId = try {
      call.parameters["info_id"]!!
    } catch (e: Exception) {
      return@get call.respond(status = HttpStatusCode.BadRequest, message = "bad URL.")
    }.toInt()

    return@get try {
      val result = getReferenceInfoItemByIdHandler.getReferenceInfoItemById(referenceInfoId)
      call.respond(status = result.statusCode, message = result.data)
    } catch (e: AppException) {
      e.printStackTrace()
      call.respond(status = e.statusCode, message = e.customMessage)
    }
  }
}