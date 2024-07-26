package com.lucasalfare.flrefs.main.domain.model

/**
 * Abstract supertype indicating a general response of a
 * CDN service. All CDN services used should be able to be
 * transformed into this. In other words, all CDN compatible
 * are those that can return a URL to the uploaded resource.
 *
 * Knowing this, their responses can be parsed to this simply.
 */
data class CdnUploadResult(
  val downloadUrl: String
)