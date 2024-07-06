package com.lucasalfare.flrefs.main

import com.lucasalfare.flbase.AppError
import io.ktor.http.*

class InitializationError(
  customMessage: String = "Error initializing stuff.",
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)