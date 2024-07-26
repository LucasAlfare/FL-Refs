package com.lucasalfare.flrefs.main.domain.model.dto

import kotlinx.serialization.Serializable

/**
 * Data transfer object representing an item response.
 *
 * @property id The unique identifier of the item.
 * @property title The title of the item.
 * @property description A description of the item.
 * @property category The category to which the item belongs.
 * @property name The name of the item.
 * @property originalUrl The URL of the original image.
 * @property thumbnailUrl The URL of the thumbnail image.
 */
@Serializable
data class ItemResponseDTO(
  var id: Int,
  var title: String,
  var description: String,
  var category: String,
  var name: String,
  var originalUrl: String,
  var thumbnailUrl: String
)