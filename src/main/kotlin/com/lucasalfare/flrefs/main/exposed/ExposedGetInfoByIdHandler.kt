package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flbase.AppResult
import com.lucasalfare.flbase.UnavailableDatabaseService
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import com.lucasalfare.flrefs.main.model.OriginalRawImage
import org.jetbrains.exposed.sql.selectAll

// TODO: damn, refactor this name
object ExposedGetInfoByIdHandler : AppServiceAdapter() {

  override suspend fun getOriginalImageById(id: Int): AppResult<OriginalRawImage> {
    val search = AppDB.exposedQuery {
      (ImagesData leftJoin ReferencesInfo)
        .selectAll()
        .where { ReferencesInfo.id eq id }
        .singleOrNull()
        .let {
          if (it == null) {
            throw UnavailableDatabaseService()
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