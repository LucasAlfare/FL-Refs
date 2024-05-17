@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model.dto

import com.lucasalfare.flrefs.main.ValidationError
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
    require(title.isNotEmpty()) { throw ValidationError() }
    require(description.isNotEmpty()) { throw ValidationError() }
    require(rawReferenceData.isNotEmpty()) { throw ValidationError() }
  }

  fun createConcatenation() = buildString {
    append(title)
    append(description)
    append(relatedFranchiseName)
  }
}