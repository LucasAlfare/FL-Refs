package com.lucasalfare.flrefs.main.exposed

import org.jetbrains.exposed.dao.id.IntIdTable

object Franchises : IntIdTable("Franchises") {
  val name = text("name").uniqueIndex()
}

object ReferencesInfo : IntIdTable("References") {
  val title = text("title").uniqueIndex()
  val description = text("description")
  val relatedFranchiseId = integer("related_franchise_id").references(Franchises.id)
  val concatenation = text("concatenation") // used in by-term filtering/selecting
}

// TODO: we must to keep track of original image format
object ImagesData : IntIdTable("ImagesData") {
  val rawReferenceData = blob("raw_reference_data")
  val rawThumbnailData = blob("raw_thumbnail_data")
  val relatedFranchiseId = integer("related_franchise_id").references(Franchises.id)
  val relatedReferenceId = integer("related_reference_id").references(ReferencesInfo.id)
}