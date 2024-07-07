package com.lucasalfare.flrefs.main

import com.lucasalfare.flbase.AppError
import io.ktor.http.*

class UnavailableCdnService(
  customMessage: String = "CDN Unavailable",
  status: HttpStatusCode = HttpStatusCode.UnprocessableEntity
) : AppError(customMessage, status)