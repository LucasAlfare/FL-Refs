package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.AppRepositoryAdapter
import com.lucasalfare.flrefs.main.model.ItemResponseDTO
import org.jetbrains.exposed.sql.selectAll

private const val MAX_PAGE_ITEMS = 10

object ImagesGetterExposed : AppRepositoryAdapter() {
  override suspend fun getAll(term: String, maxItems: Int, offset: Int) =
    AppDB.exposedQuery {
      Images
        .selectAll()
        .apply { // conditionally performs SELECT LIKE based on term
          if (term.isNotEmpty()) {
            where { Images.concatenation like "%$term%" }
          }
        }
//        .let { it -> // conditionally performs SELECT LIKE based on term
//          if (term.isNotEmpty()) it.where { Images.concatenation like "%$term%" }
//          else it
//        }
        .orderBy(Images.createdAt)
        .limit(n = if (maxItems == 0) MAX_PAGE_ITEMS else maxItems, offset.toLong())
        .map {
          ItemResponseDTO(
            id = it[Images.id].value,
            title = it[Images.title],
            description = it[Images.description],
            category = it[Images.category],
            name = it[Images.name],
            originalUrl = it[Images.originalUrl],
            thumbnailUrl = it[Images.thumbnailUrl]
          )
        }
    }
}