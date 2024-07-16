package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.cdnUploader
import com.lucasalfare.flrefs.main.exposed.ImagesInfosRepository
import com.lucasalfare.flrefs.main.exposed.ImagesUrlsRepository
import com.lucasalfare.flrefs.main.generateThumbnail
import com.lucasalfare.flrefs.main.model.UploadRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// TODO: consider inserting into DB first, then CDN upload
fun Routing.uploadItemRoute() {
  post("/uploads") {
    call.receive<UploadRequestDTO>().also { req ->
      ImagesInfosRepository.createImageInfo(req).let { imageTitle ->
        cdnUploader.upload(req.name, req.data, req.title).let { originalResult ->
          val thumbnailBytes = generateThumbnail(req.data)
          cdnUploader.upload("thumbnail-${req.name}", thumbnailBytes, req.title).let { thumbnailUploadResult ->
            ImagesUrlsRepository.createImageUrls(
              relatedImageInfoTitle = imageTitle,
              originalUrl = originalResult.content.downloadUrl,
              thumbnailUrl = thumbnailUploadResult.content.downloadUrl
            ).let {
              return@post call.respond(HttpStatusCode.Created, it)
            }
          }
        }
      }
    }
  }
}