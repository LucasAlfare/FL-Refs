@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model.dto

import kotlinx.serialization.Serializable

// TODO: we must to keep track of original image format info
@Serializable
data class UploadRequestDTO(
  val title: String,
  val description: String,
  val relatedFranchiseName: String,
  val rawReferenceData: ByteArray
) {

  fun createConcatenation() = buildString {
    append(title)
    append(description)
    append(relatedFranchiseName)
  }
}