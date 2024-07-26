package com.lucasalfare.flrefs.main.domain

interface ThumbnailGenerator {

  fun generate(imageBytes: ByteArray, width: Int = 200, height: Int = 200): ByteArray
}