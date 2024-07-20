package com.lucasalfare.flrefs.main.model.dto

import com.lucasalfare.flrefs.main.ValidationError
import com.lucasalfare.flrefs.main.localization.Message
import com.lucasalfare.flrefs.main.removeAccentuation
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Data transfer object (DTO) representing an upload request.
 *
 * @property title The title of the upload request.
 * @property description The description of the upload request.
 * @property category The category of the upload request.
 * @property name The name associated with the upload request.
 * @property data The byte array data to be uploaded.
 * @throws ValidationError If any of the required fields are blank or if the data format is not supported.
 */
@Suppress("ArrayInDataClass")
@Serializable
data class UploadRequestDTO(
  var title: String,
  var description: String,
  var category: String,
  var name: String,
  var data: ByteArray
) {

  @Transient
  lateinit var concatenation: String

  init {
    if (title.isBlank()) throw ValidationError(Message.UPLOAD_TITLE_EMPTY_ERROR.toString())
    if (description.isBlank()) throw ValidationError(Message.UPLOAD_DESCRIPTION_EMPTY_ERROR.toString())
    if (category.isBlank()) throw ValidationError(Message.UPLOAD_CATEGORY_EMPTY_ERROR.toString())
    if (name.isBlank()) throw ValidationError(Message.UPLOAD_NAME_EMPTY_ERROR.toString())
    if (data.isEmpty()) throw ValidationError(Message.UPLOAD_DATA_EMPTY_ERROR.toString())
    if (name.split(".").last().lowercase().contains("webp"))
      throw ValidationError(Message.UNSUPPORTED_UPLOAD_EXTENSION_ERROR.toString())
    if (data.size > 10_000 * 1000) throw ValidationError(Message.LARGE_PAYLOAD_ERROR.toString())

    concatenation = "$title$description$category"

    title = title.replace(" ", "_").lowercase().removeAccentuation()
    description = description.removeAccentuation()
    category = category.removeAccentuation()
    name = name.removeAccentuation()

    concatenation += "$title$description$category"
  }

  /**
   * Returns a string representation of the [UploadRequestDTO] object.
   *
   * @return A string representation of the object.
   */
  override fun toString(): String {
    return "UploadRequestDTO(title=$title, description=$description, name=$name, concatenation=$concatenation)"
  }
}