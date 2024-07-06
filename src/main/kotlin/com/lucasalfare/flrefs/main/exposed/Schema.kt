package com.lucasalfare.flrefs.main.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Images : IntIdTable("Images") {

  val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
  val name = text("name").uniqueIndex()
  val downloadUrl = text("download_url").uniqueIndex()
}