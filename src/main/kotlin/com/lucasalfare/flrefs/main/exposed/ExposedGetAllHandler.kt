package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flbase.AppResult
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import com.lucasalfare.flrefs.main.model.dto.response.ReferenceInfoItemDTO
import io.ktor.http.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll

object ExposedGetAllHandler : AppServiceAdapter() {

  override suspend fun getAllReferencesInfo(page: Int): AppResult<List<ReferenceInfoItemDTO>> {
    val pageSize = 10
    val requestedPage = page.toLong()
    val searchOffset = maxOf(0, ((requestedPage - 1) * pageSize) - 1)

    val items = AppDB.exposedQuery {
      (Franchises leftJoin ReferencesInfo leftJoin ImagesData)
        .selectAll()
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
    return AppResult(data = items, statusCode = HttpStatusCode.OK)
  }
}