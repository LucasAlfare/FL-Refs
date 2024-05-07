package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.exposed.ExposedUploadHandler
import com.lucasalfare.flrefs.main.model.dto.UploadRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.uploadRoute() {
  post("/upload") {
    val uploadRequestDTO = try {
      call.receive<UploadRequestDTO>()
    } catch (e: Exception) {
      return@post call.respond(HttpStatusCode.BadRequest, "serialization error")
    }

    return@post try {
      val result = ExposedUploadHandler.uploadReferenceImage(uploadRequestDTO)
      call.respond(status = result.statusCode, message = result.data)
    } catch (e: AppException) {
      e.printStackTrace()
      call.respond(status = e.statusCode, message = e.customMessage)
    }
  }
}