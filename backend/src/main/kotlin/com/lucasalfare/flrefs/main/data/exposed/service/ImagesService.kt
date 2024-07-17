package com.lucasalfare.flrefs.main.data.exposed.service

import com.lucasalfare.flrefs.main.data.exposed.crud.ImagesInfosCRUD
import com.lucasalfare.flrefs.main.model.UploadRequestDTO

object ImagesService {

  suspend fun createImageInfo(uploadRequestDTO: UploadRequestDTO) =
    ImagesInfosCRUD.create(
      title = uploadRequestDTO.title,
      description = uploadRequestDTO.description,
      category = uploadRequestDTO.category,
      name = uploadRequestDTO.name,
      concatenation = uploadRequestDTO.concatenation
    ).title

  // TODO: implement get images here (include urls)
}