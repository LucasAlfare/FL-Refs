package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flbase.BadRequest
import com.lucasalfare.flrefs.main.getInfoByIdHandler
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Routing.downloadRoute() {
  get("/download/{id}") {
    val referenceInfoId = try {
      call.parameters["id"]!!
    } catch (e: Exception) {
      throw BadRequest()
    }.toInt()

    val result = getInfoByIdHandler.getOriginalImageById(referenceInfoId)

    // static format?
    val resultFile = File.createTempFile("${result.data.name}_reference_image", ".png")
    resultFile.writeBytes(result.data.rawImageBytes)
    resultFile.deleteOnExit()
    return@get call.respondFile(resultFile)
  }
}