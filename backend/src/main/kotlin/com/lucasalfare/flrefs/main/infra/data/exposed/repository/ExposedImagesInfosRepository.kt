package com.lucasalfare.flrefs.main.infra.data.exposed.repository

import com.lucasalfare.flrefs.main.domain.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.domain.model.ImageInfo
import com.lucasalfare.flrefs.main.domain.repository.ImageRepository
import com.lucasalfare.flrefs.main.infra.data.exposed.AppDB
import com.lucasalfare.flrefs.main.infra.data.exposed.ImagesInfos
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


// Maximum number of items to be displayed per page
private const val MAX_PAGE_ITEMS = 10

object ExposedImagesInfosRepository : ImageRepository {

  override suspend fun create(
    title: String,
    description: String,
    category: String,
    name: String,
    concatenation: String
  ) = AppDB.exposedQuery {
    try {
      ImagesInfos.insertReturning {
        it[ImagesInfos.title] = title
        it[ImagesInfos.description] = description
        it[ImagesInfos.category] = category
        it[ImagesInfos.name] = name
        it[ImagesInfos.concatenation] = concatenation
      }.singleOrNull()!!.let {
        ImageInfo(
          id = it[ImagesInfos.id].value,
          createdAt = it[ImagesInfos.createdAt],
          title = it[ImagesInfos.title],
          description = it[ImagesInfos.description],
          category = it[ImagesInfos.category],
          name = it[ImagesInfos.name],
          concatenation = it[ImagesInfos.concatenation]
        )
      }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(Message.IMAGE_INSERTION_ERROR.toString())
    }
  }

  override suspend fun readByTitle(title: String) = AppDB.exposedQuery {
    try {
      ImagesInfos.selectAll()
        .where { ImagesInfos.title eq title }
        .mapNotNull { toImageInfo(it) }
        .singleOrNull()
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository(Message.IMAGE_SELECTION_ERROR.toString())
    }
  }

  override suspend fun readAll(
    term: String, maxItems: Int, offset: Int
  ) = AppDB.exposedQuery {
    try {
      ImagesInfos
        .selectAll()
        .apply {
          // Conditionally performs SELECT LIKE based on term
          if (term.isNotEmpty()) {
            where { ImagesInfos.concatenation like "%$term%" }
          }
        }
        .orderBy(ImagesInfos.createdAt)
        .limit(n = if (maxItems == 0) MAX_PAGE_ITEMS else maxItems, offset.toLong())
        .map { toImageInfo(it) }
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  override suspend fun update(
    title: String,
    newTitle: String?,
    newDescription: String?,
    newCategory: String?,
    newName: String?,
    newConcatenation: String?
  ) = AppDB.exposedQuery {
    try {
      ImagesInfos.update({ ImagesInfos.title eq title }) {
        if (newTitle != null) it[ImagesInfos.title] = newTitle
        if (newDescription != null) it[description] = newDescription
        if (newCategory != null) it[category] = newCategory
        if (newName != null) it[name] = newName
        if (newConcatenation != null) it[concatenation] = newConcatenation
      } > 0
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  override suspend fun delete(title: String) = AppDB.exposedQuery {
    try {
      ImagesInfos.deleteWhere { ImagesInfos.title eq title } > 0
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository()
    }
  }

  private fun toImageInfo(row: ResultRow): ImageInfo =
    ImageInfo(
      id = row[ImagesInfos.id].value,
      createdAt = row[ImagesInfos.createdAt],
      title = row[ImagesInfos.title],
      description = row[ImagesInfos.description],
      category = row[ImagesInfos.category],
      name = row[ImagesInfos.name],
      concatenation = row[ImagesInfos.concatenation]
    )
}