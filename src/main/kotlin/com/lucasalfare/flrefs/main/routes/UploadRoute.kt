package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.cdnUploader
import com.lucasalfare.flrefs.main.createImageRegistryService
import com.lucasalfare.flrefs.main.model.dto.request.UploadRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.uploadRoute() {
  post("/upload") {
    call.receive<UploadRequestDTO>().also { req ->
      cdnUploader.upload(req.name, req.data).also {
        createImageRegistryService.createImageRegistry(req.name, it.directFileAccessUrl)
        return@post call.respond(HttpStatusCode.Created, it)
      }
    }
  }
}