package com.lucasalfare.flrefs.main.model

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
    // TODO: validate entries before manipulation

    title = title.removeAccentuation()
    description = description.removeAccentuation()
    category = category.removeAccentuation()
    name = name.removeAccentuation()
  }

  override fun toString(): String {
    return "UploadRequestDTO(title=$title, description=$description, name=$name)"
  }
}