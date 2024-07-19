package com.lucasalfare.flrefs.main.model

import kotlinx.datetime.LocalDateTime

data class ImageInfo(
  val id: Int,
  val createdAt: LocalDateTime,
  val title: String,
  val description: String,
  val category: String,
  val name: String,
  val concatenation: String
)