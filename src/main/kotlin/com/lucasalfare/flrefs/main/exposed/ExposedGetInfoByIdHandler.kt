package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.AbstractAppService
import com.lucasalfare.flrefs.main.AppDB
import com.lucasalfare.flrefs.main.AppException
import com.lucasalfare.flrefs.main.AppResult
import com.lucasalfare.flrefs.main.model.OriginalRawImage
import io.ktor.http.*
import org.jetbrains.exposed.sql.selectAll

// TODO: damn, refactor this name
object ExposedGetInfoByIdHandler : AbstractAppService() {

  override suspend fun getOriginalImageById(id: Int): AppResult<OriginalRawImage> {
    val search = AppDB.query {
      (ImagesData leftJoin ReferencesInfo)
        .selectAll()
        .where { ReferencesInfo.id eq id }
        .singleOrNull()
        .let {
          if (it == null) {
            throw AppException(
              customMessage = "item not found.",
              statusCode = HttpStatusCode.NotFound
            )
          }

          OriginalRawImage(
            name = it[ReferencesInfo.title],
            rawImageBytes = it[ImagesData.rawReferenceData].bytes
          )
        }
    }

    println("VALUE FOUND WAS: ${search.name}")

    return AppResult(data = search)
  }
}