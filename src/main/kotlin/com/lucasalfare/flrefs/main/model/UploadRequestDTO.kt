package com.lucasalfare.flrefs.main.model

import com.lucasalfare.flbase.ValidationError
import com.lucasalfare.flrefs.main.removeAccentuation
import kotlinx.serialization.Serializable

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

    title = title.removeAccentuation()
    description = description.removeAccentuation()
    category = category.removeAccentuation()
    name = name.removeAccentuation()
  }

  override fun toString(): String {
    return "UploadRequestDTO(title=$title, description=$description, name=$name)"
  }
}