package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flbase.AppResult
import com.lucasalfare.flbase.UnavailableDatabaseService
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import com.lucasalfare.flrefs.main.model.dto.response.ReferenceInfoItemDTO
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll

object ExposedGetByTermHandler : AppServiceAdapter() {

  // simple in-memory caching.
  // TODO: consider using custom database/lib stuff for this
  // TODO: consider not caching the entire thumbnail bytes, only info
  private val cache = mutableMapOf<String, List<ReferenceInfoItemDTO>>()
  private val cacheMutex = Mutex()

  override suspend fun getReferencesInfoByTerm(term: String, page: Int): AppResult<List<ReferenceInfoItemDTO>> {
    val pageSize = 10
    val searchOffset = maxOf(0, ((page - 1) * pageSize) - 1L)

    val cachedItems = cache[term]
    if (cachedItems != null) {
      return AppResult(data = cachedItems, statusCode = HttpStatusCode.OK)
    }

    val items = try {
      AppDB.exposedQuery {
        (Franchises leftJoin ReferencesInfo leftJoin ImagesData)
          .selectAll()
          .where { ReferencesInfo.concatenation like "%$term%" }
          .orderBy(ReferencesInfo.id to SortOrder.ASC)
          .limit(n = pageSize, offset = searchOffset)
          .map {
            ReferenceInfoItemDTO(
              referenceId = it[ReferencesInfo.id].value,
              title = it[ReferencesInfo.title],
              description = it[ReferencesInfo.description],
              franchiseName = it[Franchises.name],
              rawThumbnailData = it[ImagesData.rawThumbnailData].bytes
            )
          }
      }
    } catch (e: Exception) {
      throw UnavailableDatabaseService()
    }

    try {
      cacheMutex.withLock { cache[term] = items }
    } catch (e: Exception) {
      throw UnavailableDatabaseService(customMessage = "Server can not to cache search by term.")
    }

    return AppResult(data = items, statusCode = HttpStatusCode.OK)
  }
}