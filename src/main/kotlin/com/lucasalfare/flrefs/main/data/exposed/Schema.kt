package com.lucasalfare.flrefs.main.data.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Images : IntIdTable("Images") {

  val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
  val title = text("title").uniqueIndex()
  val description = text("description")
  val category = text("category")
  val name = text("name")
  val originalUrl = text("original_url")
  val thumbnailUrl = text("thumbnail__url")
  val concatenation = text("concatenation") // title + description + category
  // TODO: consider implementing storage of image path inside CDN
}