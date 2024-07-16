package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.model.UploadRequestDTO
import org.jetbrains.exposed.sql.insert

object ImagesInfosRepository {

  suspend fun createImageInfo(uploadRequestDTO: UploadRequestDTO) = AppDB.exposedQuery {
    try {
      ImagesInfos.insert {
        it[title] = uploadRequestDTO.title
        it[description] = uploadRequestDTO.description
        it[category] = uploadRequestDTO.category
        it[name] = uploadRequestDTO.name
        it[concatenation] = uploadRequestDTO.concatenation
      }[ImagesInfos.title]
    } catch (e: Exception) {
      throw UnavailableDatabaseRepository("Can not to insert image info in the database.")
    }
  }
}