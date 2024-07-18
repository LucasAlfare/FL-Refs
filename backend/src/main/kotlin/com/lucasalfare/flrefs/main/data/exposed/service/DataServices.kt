package com.lucasalfare.flrefs.main.data.exposed.service

import com.lucasalfare.flrefs.main.cdnUploader
import com.lucasalfare.flrefs.main.data.exposed.crud.ImagesInfosCRUD
import com.lucasalfare.flrefs.main.data.exposed.crud.ImagesUrlsCRUD
import com.lucasalfare.flrefs.main.generateThumbnail
import com.lucasalfare.flrefs.main.model.ItemResponseDTO
import com.lucasalfare.flrefs.main.model.UploadRequestDTO
import com.lucasalfare.githubwrapper.main.GithubUploadResponseDTO

object DataServices {

  suspend fun createImageInfo(uploadRequestDTO: UploadRequestDTO) =
    ImagesInfosCRUD.create(
      title = uploadRequestDTO.title,
      description = uploadRequestDTO.description,
      category = uploadRequestDTO.category,
      name = uploadRequestDTO.name,
      concatenation = uploadRequestDTO.concatenation
    ).title

  suspend fun createImageUrls(
    relatedImageInfoTitle: String,
    originalUrl: String,
    thumbnailUrl: String
  ) = ImagesUrlsCRUD.create(
    relatedImageInfoTitle = relatedImageInfoTitle,
    originalUrl = originalUrl,
    thumbnailUrl = thumbnailUrl
  ).id

  suspend fun getAllDataAsItemResponseDTO(term: String = "", maxItems: Int = 0, offset: Int = 0) {
    ImagesInfosCRUD.readAll(term, maxItems, offset).map {
      val relatedUrls = ImagesUrlsCRUD.read(relatedImageInfoTitle = it.name)!!

      ItemResponseDTO(
        id = it.id,
        title = it.title,
        description = it.description,
        category = it.category,
        name = it.name,
        originalUrl = relatedUrls.originalUrl,
        thumbnailUrl = relatedUrls.thumbnailUrl
      )
    }
  }

  suspend fun uploadImageToCdn(req: UploadRequestDTO, isThumbnail: Boolean = false): GithubUploadResponseDTO {
    val nextNameAndBytes =
      when {
        isThumbnail -> Pair(
          "thumbnail-${req.name}",
          generateThumbnail(req.data)
        )

        else -> Pair(
          req.name,
          req.data
        )
      }

    return cdnUploader.upload(
      nextNameAndBytes.first,
      nextNameAndBytes.second,
      req.title
    )
  }
}