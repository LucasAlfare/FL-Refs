package com.lucasalfare.flrefs.main.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ImagesInfos : IntIdTable("ImagesInfos") {

  val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
  val title = text("title").uniqueIndex()
  val description = text("description")
  val category = text("category")
  val name = text("name")
  val concatenation = text("concatenation") // title + description + category (with and without accentuation)
  // TODO: consider implementing storage of image path inside CDN
}

object ImagesUrls : IntIdTable("ImagesUrls") {

  val relatedImageInfoTitle = text("related_image_info_title").references(ImagesInfos.title)
  val originalUrl = text("original_url")
  val thumbnailUrl = text("thumbnail_url")
}
