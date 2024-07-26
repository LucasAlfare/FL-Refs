package com.lucasalfare.flrefs.main.infra.ktor.routes

import com.lucasalfare.flrefs.main.domain.model.dto.UploadRequestDTO
import com.lucasalfare.flrefs.main.usecase.DataServices
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines a route for uploading an item with associated image data.
 *
 * This route handles POST requests to upload an image and associated information,
 * including creating image info, uploading to a CDN, generating a thumbnail,
 * and creating image URLs.
 */
fun Route.uploadItemRoute() {
  post("/uploads") {
    call.receive<UploadRequestDTO>().let { req ->
      DataServices.createImageUrls(
        DataServices.createImageInfo(req),
        DataServices.uploadImageToCdn(req, false).downloadUrl,
        DataServices.uploadImageToCdn(req, true).downloadUrl
      ).let {
        return@post call.respond(HttpStatusCode.Created, it)
      }
    }
  }
}