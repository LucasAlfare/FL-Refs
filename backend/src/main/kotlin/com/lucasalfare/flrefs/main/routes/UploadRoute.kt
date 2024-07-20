package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.data.exposed.service.DataServices.createImageInfo
import com.lucasalfare.flrefs.main.data.exposed.service.DataServices.createImageUrls
import com.lucasalfare.flrefs.main.data.exposed.service.DataServices.uploadImageToCdn
import com.lucasalfare.flrefs.main.model.dto.UploadRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
      createImageUrls(
        createImageInfo(req),
        uploadImageToCdn(req, false).downloadUrl,
        uploadImageToCdn(req, true).downloadUrl
      ).let {
        return@post call.respond(HttpStatusCode.Created, it)
      }
    }
  }
}