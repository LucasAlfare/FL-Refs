package com.lucasalfare.flrefs.main.data.exposed

import com.lucasalfare.flrefs.main.model.ItemResponseDTO
import org.jetbrains.exposed.sql.selectAll

// Maximum number of items to be displayed per page
private const val MAX_PAGE_ITEMS = 10

/**
 * Service object responsible for fetching image data.
 */
object ImagesGetterService {

  /**
   * Retrieves a list of images based on the search term, maximum number of items, and offset.
   *
   * @param term The search term used to filter images.
   * @param maxItems The maximum number of items to retrieve. If set to 0, the default value of MAX_PAGE_ITEMS will be used.
   * @param offset The offset from where to start retrieving items.
   * @return A list of [ItemResponseDTO] containing image data.
   */
  suspend fun getAll(term: String, maxItems: Int, offset: Int) =
    AppDB.exposedQuery {
      (ImagesInfos leftJoin ImagesUrls)
        .selectAll()
        .apply {
          // Conditionally performs SELECT LIKE based on term
          if (term.isNotEmpty()) {
            where { ImagesInfos.concatenation like "%$term%" }
          }
        }
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