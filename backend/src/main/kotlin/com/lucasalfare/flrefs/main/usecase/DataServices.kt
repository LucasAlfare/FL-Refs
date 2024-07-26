package com.lucasalfare.flrefs.main.usecase

import com.lucasalfare.flrefs.main.domain.CdnUploader
import com.lucasalfare.flrefs.main.domain.ThumbnailGenerator
import com.lucasalfare.flrefs.main.domain.model.CdnUploadResult
import com.lucasalfare.flrefs.main.domain.model.dto.ItemResponseDTO
import com.lucasalfare.flrefs.main.domain.model.dto.UploadRequestDTO
import com.lucasalfare.flrefs.main.domain.repository.ImageRepository
import com.lucasalfare.flrefs.main.domain.repository.ImageUrlsRepository

object DataServices {

  lateinit var imageRepository: ImageRepository
  lateinit var imageUrlsRepository: ImageUrlsRepository
  lateinit var cdnUploader: CdnUploader
  lateinit var thumbnailGenerator: ThumbnailGenerator

  suspend fun createImageInfo(uploadRequestDTO: UploadRequestDTO) =
    imageRepository.create(
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
  ) = imageUrlsRepository.create(
    relatedImageInfoTitle = relatedImageInfoTitle,
    originalUrl = originalUrl,
    thumbnailUrl = thumbnailUrl
  ).id

  suspend fun getAllDataAsItemResponseDTO(term: String = "", maxItems: Int = 0, offset: Int = 0) =
    imageRepository.readAll(term, maxItems, offset).map {
      val relatedUrls = imageUrlsRepository.readByRelatedImageTitle(relatedImageInfoTitle = it.title)

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

  suspend fun uploadImageToCdn(req: UploadRequestDTO, isThumbnail: Boolean = false): CdnUploadResult {
    val nextNameAndBytes =
      when {
        isThumbnail -> Pair(
          "thumbnail-${req.name}",
          thumbnailGenerator.generate(req.data)
        )

        else -> Pair(req.name, req.data)
      }

    return cdnUploader.upload(
      nextNameAndBytes.first,
      nextNameAndBytes.second,
      req.title
    )
  }
}