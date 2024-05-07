@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model.dto

import kotlinx.serialization.Serializable

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