package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.cdnUploader
import com.lucasalfare.flrefs.main.generateThumbnail
import com.lucasalfare.flrefs.main.imagesInserter
import com.lucasalfare.flrefs.main.model.UploadRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Suppress("UNUSED_PARAMETER")
fun Routing.uploadRoute(application: Application) {
  post("/uploads") {
    call.receive<UploadRequestDTO>().also { req ->

      // uploads original to CDN
      cdnUploader.upload(
        name = req.name, data = req.data, targetPath = req.title
      ).also { originalCdnResult ->
        val thumbnailBytes = generateThumbnail(req.data)
        // uploads its generated thumbnail to CDN
        cdnUploader.upload(
          name = "thumbnail-${req.name}", data = thumbnailBytes, targetPath = req.title
        ).also { thumbnailCdnResult ->
          // insert info in DB
          imagesInserter.doInsert(
            title = req.title,
            description = req.description,
            category = req.category,
            name = req.name,
            originalUrl = originalCdnResult.content.downloadUrl,
            thumbnailUrl = thumbnailCdnResult.content.downloadUrl
          ).also { originalAndThumbnailDownloadUrl ->
            // respond to client
            return@post call.respond(
              status = HttpStatusCode.Created,
              message = originalAndThumbnailDownloadUrl
            )
          }
        }
      }
    }
  }
}