package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.getInfoByIdHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Routing.downloadRoute() {
  get("/download/{id}") {
    val referenceInfoId = try {
      call.parameters["id"]!!
    } catch (e: Exception) {
      return@get call.respond(status = HttpStatusCode.BadRequest, message = "bad URL.")
    }.toInt()

    return@get try {
      val result = getInfoByIdHandler.getOriginalImageById(referenceInfoId)

      // static format?
      val resultFile = File.createTempFile("${result.data.name}_reference_image", ".png")
      resultFile.writeBytes(result.data.rawImageBytes)
      resultFile.deleteOnExit()

      call.respondFile(resultFile)
    } catch (e: AppException) {
      e.printStackTrace()
      call.respond(status = e.statusCode, message = e.customMessage)
    }
  }
}