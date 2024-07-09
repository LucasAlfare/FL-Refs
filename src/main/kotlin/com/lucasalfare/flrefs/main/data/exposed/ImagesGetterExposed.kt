package com.lucasalfare.flrefs.main.data.exposed

import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import com.lucasalfare.flrefs.main.model.ItemResponseDTO
import org.jetbrains.exposed.sql.selectAll

private const val MAX_PAGE_ITEMS = 10

object ImagesGetterExposed : AppServiceAdapter() {
  override suspend fun getAll(term: String, maxItems: Int, offset: Int) =
    AppDB.exposedQuery {
      Images
        .selectAll()
        .let { it -> // conditionally performs SELECT LIKE based on term
          if (term.isNotEmpty()) it.where { Images.concatenation like "%$term%" }
          else it
        }
        .orderBy(Images.createdAt)
        .limit(n = if (maxItems == 0) MAX_PAGE_ITEMS else maxItems, offset.toLong())
        .map {
          ItemResponseDTO(
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