@file:Suppress("ArrayInDataClass")

package com.lucasalfare.flrefs.main.model.dto.request

import com.lucasalfare.flbase.SerializationError
import kotlinx.serialization.Serializable

@Serializable
data class UploadRequestDTO(
  val name: String,
  val data: ByteArray
) {
  init {
    if (name.isEmpty()) {
      throw SerializationError(customMessage = "Input name is empty.")
    }

    // checks in kilobytes range
    if (data.size !in 1..(15_000 * 1000)) {
      throw SerializationError(customMessage = "Input data size is higher than allowed.")
    }
  }

  override fun toString() =
    "UploadRequestDTO(name=$name, data=\"${if (data.isNotEmpty()) "[...]" else "[empty}"}\")"
}