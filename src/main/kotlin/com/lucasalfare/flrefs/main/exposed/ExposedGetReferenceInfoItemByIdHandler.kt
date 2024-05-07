package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.AbstractAppService
import com.lucasalfare.flrefs.main.AppDB
import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.AppResult
import com.lucasalfare.flrefs.main.model.OriginalRawImage
import com.lucasalfare.flrefs.main.model.ReferenceInfoItem
import io.ktor.http.*
import org.jetbrains.exposed.sql.selectAll

// TODO: damn, refactor this name
object ExposedGetReferenceInfoItemByIdHandler : AbstractAppService() {

  override suspend fun getReferenceInfoItemById(id: Int): AppResult<OriginalRawImage> {
    val search = AppDB.query {
      ImagesData
        .selectAll()
        .where { ImagesData.relatedReferenceId eq id }
        .singleOrNull()
        .let {
          if (it == null) {
            throw AppException(customMessage = "item not found.", statusCode = HttpStatusCode.NotFound)
          }

          OriginalRawImage(
            rawImageBytes = it[ImagesData.rawReferenceData].bytes
          )
        }
    }

    return AppResult(data = search)
  }
}