package com.lucasalfare.flrefs.main.data.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Users : IntIdTable("Users") {

  val email = text("email").uniqueIndex()
  val hashedPassword = text("hashed_password")
}

/**
 * Table object representing the "ImagesInfos" table in the database.
 */
object ImagesInfos : IntIdTable("ImagesInfos") {

  /**
   * The timestamp when the image information was created.
   */
  val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

  /**
   * The title of the image. This field is unique and indexed.
   */
  val title = text("title").uniqueIndex()

  /**
   * The description of the image.
   */
  val description = text("description")

  /**
   * The category of the image.
   */
  val category = text("category")

  /**
   * The name of the image.
   */
  val name = text("name")

  /**
   * A concatenation of title, description, and category. This is used for search purposes.
   */
  val concatenation = text("concatenation") // title + description + category (with and without accentuation)

  // TODO: consider implementing storage of image path inside CDN
}

/**
 * Table object representing the "ImagesUrls" table in the database.
 */
object ImagesUrls : IntIdTable("ImagesUrls") {

  /**
   * The title of the related image information. This is a foreign key referencing the title in the ImagesInfos table.
   */
  val relatedImageInfoTitle = text("related_image_info_title").references(ImagesInfos.title)

  /**
   * The URL of the original image.
   */
  val originalUrl = text("original_url")

  /**
   * The URL of the thumbnail image.
   */
  val thumbnailUrl = text("thumbnail_url")
}