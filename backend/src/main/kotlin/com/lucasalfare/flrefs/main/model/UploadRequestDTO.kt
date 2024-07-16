package com.lucasalfare.flrefs.main.model

import com.lucasalfare.flrefs.main.ValidationError
import com.lucasalfare.flrefs.main.removeAccentuation
import kotlinx.serialization.Serializable

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

  init {
    if (title.isBlank()) throw ValidationError("Title cannot be blank")
    if (description.isBlank()) throw ValidationError("Description cannot be blank")
    if (category.isBlank()) throw ValidationError("Category cannot be blank")
    if (name.isBlank()) throw ValidationError("Name cannot be blank")
    if (data.isEmpty()) throw ValidationError("Data cannot be empty")
    if (name.split(".").last().lowercase().contains("webp"))
      throw ValidationError("WEBP format is not supported yet.")

    title = title.replace(" ", "_").lowercase().removeAccentuation()
    description = description.removeAccentuation()
    category = category.removeAccentuation()
    name = name.removeAccentuation()
  }

  /**
   * Generates a concatenation of title, description, and category.
   *
   * @return A string concatenation of title, description, and category.
   */
  fun getConcatenation() = buildString { append(title); append(description); append(category) }

  /**
   * Returns a string representation of the [UploadRequestDTO] object.
   *
   * @return A string representation of the object.
   */
  override fun toString(): String {
    return "UploadRequestDTO(title=$title, description=$description, name=$name)"
  }
}