package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flbase.SerializationError
import com.lucasalfare.flrefs.main.model.dto.request.UploadRequestDTO
import com.lucasalfare.flrefs.main.uploadHandler
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.uploadRoute() {
  post("/upload") {
    val uploadRequestDTO = try {
      call.receive<UploadRequestDTO>()
    } catch (e: Exception) {
      throw SerializationError()
    }

    val result = uploadHandler.uploadReferenceImage(uploadRequestDTO)
    return@post call.respond(status = result.statusCode, message = result.data)
  }
}