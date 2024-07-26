package com.lucasalfare.flrefs.main.domain.repository

import com.lucasalfare.flrefs.main.domain.model.ImageInfo

interface ImageRepository {

  suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    concatenation: String
  ): ImageInfo

  suspend fun readByTitle(title: String): ImageInfo?

  suspend fun readAll(term: String = "", maxItems: Int = 0, offset: Int = 0): List<ImageInfo>

  suspend fun update(
    title: String,
    newTitle: String? = null,
    newDescription: String? = null,
    newCategory: String? = null,
    newName: String? = null,
    newConcatenation: String? = null
  ): Boolean

  suspend fun delete(title: String): Boolean
}