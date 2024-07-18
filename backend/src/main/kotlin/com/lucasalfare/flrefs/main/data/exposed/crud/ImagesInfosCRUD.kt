package com.lucasalfare.flrefs.main.data.exposed.crud

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


// Maximum number of items to be displayed per page
private const val MAX_PAGE_ITEMS = 10

data class ImageInfo(
  val id: Int,
  val createdAt: LocalDateTime,
  val title: String,
  val description: String,
  val category: String,
  val name: String,
  val concatenation: String
)

object ImagesInfosCRUD {

  suspend fun create(
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
      throw UnavailableDatabaseRepository("Can not to insert Image Info data")
    }
  }

  suspend fun read(title: String) = AppDB.exposedQuery {
    try {
      ImagesInfos.selectAll()
        .where { ImagesInfos.title eq title }
        .mapNotNull { toImageInfo(it) }
        .singleOrNull()
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository("Can not to get/select the Image Info from database.")
    }
  }

  suspend fun readAll(
    term: String = "",
    maxItems: Int = 0,
    offset: Int = 0
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

  suspend fun update(
    title: String,
    newTitle: String? = null,
    newDescription: String? = null,
    newCategory: String? = null,
    newName: String? = null,
    newConcatenation: String? = null
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

  suspend fun delete(title: String) = AppDB.exposedQuery {
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