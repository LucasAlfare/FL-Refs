package com.lucasalfare.flrefs.main

import io.ktor.http.*

open class AppError(
  val customMessage: String?,
  val status: HttpStatusCode = HttpStatusCode.InternalServerError
) : Throwable()

class UnavailableDatabaseService(
  customMessage: String = "Error performing database operation.",
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)

class BadRequest(
  customMessage: String = "Error in the requested payload.",
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

class SerializationError(
  customMessage: String = "Error in serialization process.",
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

class ValidationError(
  customMessage: String = "Error in validation of fields.",
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)