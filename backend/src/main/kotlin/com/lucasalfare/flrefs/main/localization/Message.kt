package com.lucasalfare.flrefs.main.localization

import com.lucasalfare.flrefs.main.currentLocale
import java.util.*

@Suppress("unused")
enum class Message {
  GENERAL_DATABASE_ERROR,
  GENERAL_BAD_REQUEST,
  GENERAL_SERIALIZATION_ERROR,
  GENERAL_VALIDATION_ERROR,
  GENERAL_MISSING_ENV_VAR_ERROR,
  GENERAL_EMPTY_ENV_VAR_ERROR,
  GENERAL_CDN_UNAVAILABLE,
  DB_USER_SELECTION_ERROR,
  DB_CLEAR_SUCCESS,
  BAD_GET_QUERY_PARAMS,
  LARGE_PAYLOAD_ERROR,
  UPLOAD_TITLE_EMPTY_ERROR,
  UPLOAD_DESCRIPTION_EMPTY_ERROR,
  UPLOAD_CATEGORY_EMPTY_ERROR,
  UPLOAD_NAME_EMPTY_ERROR,
  UPLOAD_DATA_EMPTY_ERROR,
  UNSUPPORTED_UPLOAD_EXTENSION_ERROR,
  AUTHENTICATION_ERROR,
  IMAGE_URL_INSERTION_ERROR,
  IMAGE_URL_SELECTION_ERROR,
  IMAGE_INSERTION_ERROR,
  IMAGE_SELECTION_ERROR,
  CDN_UPLOAD_ERROR;

  private val bundleBaseName = "i18n.messages"

  // permits usage with string that contains placeholders
  fun format(vararg args: Any): String {
    val message = toString()
    return String.format(message, *args)
  }

  override fun toString(): String {
    val bundle = ResourceBundle.getBundle(bundleBaseName, currentLocale)
    return bundle.getString(this.name)
  }
}