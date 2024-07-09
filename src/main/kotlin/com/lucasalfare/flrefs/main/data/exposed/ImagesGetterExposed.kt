package com.lucasalfare.flrefs.main.data.exposed

import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import com.lucasalfare.flrefs.main.model.ItemResponseDTO
import org.jetbrains.exposed.sql.selectAll

object ImagesGetterExposed : AppServiceAdapter() {
  override suspend fun getAll(maxItems: Int, offset: Int) =
    AppDB.exposedQuery {
      Images
        .selectAll()
        .orderBy(Images.createdAt)
        .limit(n = maxItems, offset.toLong())
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