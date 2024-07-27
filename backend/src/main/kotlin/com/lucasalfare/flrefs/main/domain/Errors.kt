package com.lucasalfare.flrefs.main.domain


import com.lucasalfare.flrefs.main.domain.localization.Message

/**
 * Represents a general application error with a custom message and an associated HTTP status.
 *
 * This class serves as a base class for more specific error types.
 *
 * @property customMessage A custom message describing the error.
 * @property status The HTTP status code associated with the error. The default is `HttpStatusCode.InternalServerError`.
 */
open class AppError(
  val customMessage: String = ""
) : Throwable(message = customMessage)

class UnavailableDatabaseRepository(
  customMessage: String = Message.GENERAL_DATABASE_ERROR.toString()
) : AppError(customMessage)

class BadRequest(
  customMessage: String = Message.GENERAL_BAD_REQUEST.toString()
) : AppError(customMessage)

class SerializationError(
  customMessage: String = Message.GENERAL_SERIALIZATION_ERROR.toString()
) : AppError(customMessage)

class ValidationError(
  customMessage: String = Message.GENERAL_VALIDATION_ERROR.toString()
) : AppError(customMessage)

class NullEnvironmentVariable(
  customMessage: String = Message.GENERAL_MISSING_ENV_VAR_ERROR.toString()
) : AppError(customMessage)

class EmptyEnvironmentVariable(
  customMessage: String = Message.GENERAL_EMPTY_ENV_VAR_ERROR.toString()
) : AppError(customMessage)

class UnavailableCdnService(
  customMessage: String = Message.GENERAL_CDN_UNAVAILABLE.toString()
) : AppError(customMessage)

class AuthorizationError(
  customMessage: String = Message.GENERAL_UNAUTHORIZED_ERROR.toString()
) : AppError(customMessage)