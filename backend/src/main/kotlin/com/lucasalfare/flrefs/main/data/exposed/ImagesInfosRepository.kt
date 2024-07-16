package com.lucasalfare.flrefs.main.data.exposed

import com.lucasalfare.flrefs.main.UnavailableDatabaseRepository
import com.lucasalfare.flrefs.main.model.UploadRequestDTO
import org.jetbrains.exposed.sql.insert

/**
 * Repository object responsible for handling image information data operations.
 */
object ImagesInfosRepository {

  /**
   * Inserts a new image information record into the database.
   *
   * @param uploadRequestDTO The data transfer object containing the image information to be inserted.
   * @return The title of the inserted image information.
   * @throws UnavailableDatabaseRepository if the database is unavailable or an error occurs during the insertion.
   */
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
      throw UnavailableDatabaseRepository("Cannot insert image info in the database.")
    }
  }
}