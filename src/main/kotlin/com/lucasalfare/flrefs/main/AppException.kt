package com.lucasalfare.flrefs.main

import io.ktor.http.*

// TODO: change this approach to a better one
data class AppException(
  var customMessage: String = "",
  var statusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
  val parentException: Exception? = null
) : Exception() {

  init {
    if (parentException != null) {
      val last = customMessage
      customMessage = buildString {
        append("Custom message:\n")
        append(last)
        append("\n")
        append(parentException.stackTrace.contentToString())
      }
    }
  }
}