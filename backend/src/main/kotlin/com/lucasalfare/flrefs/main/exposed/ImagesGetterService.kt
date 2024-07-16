package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.model.ItemResponseDTO
import org.jetbrains.exposed.sql.selectAll

private const val MAX_PAGE_ITEMS = 10

object ImagesGetterService {

  suspend fun getAll(term: String, maxItems: Int, offset: Int) =
    AppDB.exposedQuery {
      (ImagesInfos leftJoin ImagesUrls)
        .selectAll()
        .apply { // conditionally performs SELECT LIKE based on term
          if (term.isNotEmpty()) {
            where { ImagesInfos.concatenation like "%$term%" }
          }
        }
//        .let { it -> // conditionally performs SELECT LIKE based on term
//          if (term.isNotEmpty()) it.where { Images.concatenation like "%$term%" }
//          else it
//        }
        .orderBy(ImagesInfos.createdAt)
        .limit(n = if (maxItems == 0) MAX_PAGE_ITEMS else maxItems, offset.toLong())
        .map {
          ItemResponseDTO(
            id = it[ImagesInfos.id].value,
            title = it[ImagesInfos.title],
            description = it[ImagesInfos.description],
            category = it[ImagesInfos.category],
            name = it[ImagesInfos.name],
            originalUrl = it[ImagesUrls.originalUrl],
            thumbnailUrl = it[ImagesUrls.thumbnailUrl]
          )
        }
    }
}