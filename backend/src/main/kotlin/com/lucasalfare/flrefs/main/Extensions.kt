package com.lucasalfare.flrefs.main

import java.text.Normalizer

/**
 * Custom function to get root [Throwable] cause.
 *
 * This is used in order to not use the same function of the Ktor API due to it
 * be marked with [@InternalApi].
 */
fun Throwable.customRootCause(): Throwable {
  return if (cause == null) this else cause!!.customRootCause()
}

fun String.removeAccentuation() =
  Normalizer.normalize(
    this,
    Normalizer.Form.NFD
  ).replace(
    regex = Regex("\\p{InCombiningDiacriticalMarks}+"),
    replacement = ""
  )