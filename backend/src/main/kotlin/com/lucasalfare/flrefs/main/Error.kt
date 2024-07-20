package com.lucasalfare.flrefs.main


import com.lucasalfare.flrefs.main.localization.Message
import io.ktor.http.*

/**
 * Represents a general application error with a custom message and an associated HTTP status.
 *
 * This class serves as a base class for more specific error types.
 *
 * @property customMessage A custom message describing the error.
 * @property status The HTTP status code associated with the error. The default is `HttpStatusCode.InternalServerError`.
 */
open class AppError(
  val customMessage: String = "",
  val status: HttpStatusCode = HttpStatusCode.InternalServerError
) : Throwable(message = customMessage)

/**
 * Represents an error that occurs when the database service is unavailable.
 *
 * @param customMessage A custom message describing the error. The default is "Error performing database operation."
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.InternalServerError`.
 */
class UnavailableDatabaseRepository(
  customMessage: String = Message.GENERAL_DATABASE_ERROR.toString(),
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)

/**
 * Represents an error that occurs due to a bad request.
 *
 * @param customMessage A custom message describing the error. The default is "Error in the requested payload."
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.BadRequest`.
 */
class BadRequest(
  customMessage: String = Message.GENERAL_BAD_REQUEST.toString(),
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

/**
 * Represents an error that occurs during the serialization process.
 *
 * @param customMessage A custom message describing the error. The default is "Error in serialization process."
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.BadRequest`.
 */
class SerializationError(
  customMessage: String = Message.GENERAL_SERIALIZATION_ERROR.toString(),
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

/**
 * Represents an error that occurs during the validation of fields.
 *
 * @param customMessage A custom message describing the error. The default is "Error in validation of fields."
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.BadRequest`.
 */
class ValidationError(
  customMessage: String = Message.GENERAL_VALIDATION_ERROR.toString(),
  status: HttpStatusCode = HttpStatusCode.BadRequest
) : AppError(customMessage, status)

/**
 * Represents an error that occurs when a required environment variable is null.
 *
 * @param customMessage A custom message describing the error. The default is "Missing an environment variable in server (null)".
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.InternalServerError`.
 */
class NullEnvironmentVariable(
  customMessage: String = Message.GENERAL_MISSING_ENV_VAR_ERROR.toString(),
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)

/**
 * Represents an error that occurs when a required environment variable is empty.
 *
 * @param customMessage A custom message describing the error. The default is "Missing an environment variable in server (empty)".
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.InternalServerError`.
 */
class EmptyEnvironmentVariable(
  customMessage: String = Message.GENERAL_EMPTY_ENV_VAR_ERROR.toString(),
  status: HttpStatusCode = HttpStatusCode.InternalServerError
) : AppError(customMessage, status)

/**
 * Represents an error that occurs when a CDN service is unavailable.
 *
 * @param customMessage A custom message describing the error. The default is "CDN Unavailable".
 * @param status The HTTP status code associated with the error. The default is `HttpStatusCode.UnprocessableEntity`.
 */
class UnavailableCdnService(
  customMessage: String = Message.GENERAL_CDN_UNAVAILABLE.toString(),
  status: HttpStatusCode = HttpStatusCode.UnprocessableEntity
) : AppError(customMessage, status)