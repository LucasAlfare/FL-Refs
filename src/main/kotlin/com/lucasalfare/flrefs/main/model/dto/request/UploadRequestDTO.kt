@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model.dto.request

import com.lucasalfare.flbase.ValidationError
import kotlinx.serialization.Serializable

// TODO: we must to keep track of original image format info
@Serializable
data class UploadRequestDTO(
  val title: String,
  val description: String,
  val relatedFranchiseName: String,
  val rawReferenceData: ByteArray
) {

  init {
    val conditions = arrayOf(
      if (title.isEmpty()) "title is empty" else "",
      if (description.isEmpty()) "title is empty" else "",
      if (rawReferenceData.isEmpty()) "title is empty" else "",
    )

    require(conditions.all { it == "" }) {
      throw ValidationError(customMessage = conditions.map { it }.toString())
    }
  }

  fun createConcatenation() = buildString {
    append(title)
    append(description)
    append(relatedFranchiseName)
  }
}