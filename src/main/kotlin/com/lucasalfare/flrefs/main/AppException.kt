package com.lucasalfare.flrefs.main

import io.ktor.http.*

data class AppException(
  val customMessage: String = "",
  val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError
) : Exception()