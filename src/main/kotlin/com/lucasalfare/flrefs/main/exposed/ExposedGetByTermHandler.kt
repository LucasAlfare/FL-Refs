package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.*
import com.lucasalfare.flrefs.main.model.ReferenceInfoItem
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll

object ExposedGetByTermHandler : AbstractAppService() {

  // simple in-memory caching. TODO: consider using custom database/lib stuff for this
  private val cache = mutableMapOf<String, List<ReferenceInfoItem>>()
  private val cacheMutex = Mutex()

  override suspend fun getReferencesInfoByTerm(term: String, page: Int): AppResult<List<ReferenceInfoItem>> {
    val pageSize = 10
    val searchOffset = maxOf(0, ((page - 1) * pageSize) - 1L)

    val cachedItems = cache[term]
    if (cachedItems != null) {
      return AppResult(data = cachedItems, statusCode = HttpStatusCode.OK)
    }

    val items = try {
      AppDB.query {
        (Franchises leftJoin ReferencesInfo leftJoin ImagesData)
          .selectAll()
          .where { ReferencesInfo.concatenation like "%$term%" }
          .orderBy(ReferencesInfo.id to SortOrder.ASC)
          .limit(n = pageSize, offset = searchOffset)
          .map {
            ReferenceInfoItem(
              referenceId = it[ReferencesInfo.id].value,
              title = it[ReferencesInfo.title],
              description = it[ReferencesInfo.description],
              franchiseName = it[Franchises.name],
              rawThumbnailData = it[ImagesData.rawThumbnailData].bytes
            )
          }
      }
    } catch (e: Exception) {
      throw AppException(
        statusCode = HttpStatusCode.InternalServerError,
        customMessage = "error selecting items by term",
        parentException = e
      )
    }

    cacheMutex.withLock { cache[term] = items }

    return AppResult(data = items, statusCode = HttpStatusCode.OK)
  }
}