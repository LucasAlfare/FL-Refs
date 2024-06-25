package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flbase.AppResult
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

object ExposedDeleteByIdHandler : AppServiceAdapter() {

  override suspend fun deleteRegistryById(id: Int): AppResult<Unit> {
    AppDB.exposedQuery {
      ReferencesInfo.deleteWhere { ReferencesInfo.id eq id }
      ImagesData.deleteWhere { relatedReferenceId eq id }
    }

    return AppResult(Unit)
  }
}