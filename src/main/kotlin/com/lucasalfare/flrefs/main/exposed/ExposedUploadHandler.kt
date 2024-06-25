package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flbase.AppResult
import com.lucasalfare.flbase.UnavailableDatabaseService
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import com.lucasalfare.flrefs.main.ImageUtil
import com.lucasalfare.flrefs.main.model.dto.request.UploadRequestDTO
import io.ktor.http.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

object ExposedUploadHandler : AppServiceAdapter() {

  override suspend fun uploadReferenceImage(uploadRequestDTO: UploadRequestDTO): AppResult<Int> {
    return try {
      AppDB.exposedQuery {
        // try get/create franchise
        val franchiseId = try {
          Franchises
            .selectAll()
            .where { Franchises.name eq uploadRequestDTO.relatedFranchiseName }
            .singleOrNull()
            .let { result ->
              if (result != null) {
                result[Franchises.id].value
              } else {
                Franchises.insertAndGetId { it[name] = uploadRequestDTO.relatedFranchiseName }.value
              }
            }
        } catch (e: Exception) {
          throw UnavailableDatabaseService()
        }

        // try insert reference info
        val referenceId = try {
          ReferencesInfo.insertAndGetId {
            it[title] = uploadRequestDTO.title
            it[description] = uploadRequestDTO.description
            it[relatedFranchiseId] = franchiseId
            it[concatenation] = uploadRequestDTO.createConcatenation()
          }.value
        } catch (e: Exception) {
          throw UnavailableDatabaseService()
        }

        // try to generate thumbnail
        val thumbnailBytes = try {
          ImageUtil.generateThumbnail(uploadRequestDTO.rawReferenceData)
        } catch (e: Exception) {
          throw UnavailableDatabaseService()
        }

        // try to insert binary data
        ImagesData.insert {
          it[rawReferenceData] = ExposedBlob(uploadRequestDTO.rawReferenceData)
          it[rawThumbnailData] = ExposedBlob(thumbnailBytes)
          it[relatedFranchiseId] = franchiseId
          it[relatedReferenceId] = referenceId
        }
        return@exposedQuery AppResult(data = referenceId, statusCode = HttpStatusCode.Created)
      }
    } catch (e: Exception) {
      throw UnavailableDatabaseService()
    }
  }
}