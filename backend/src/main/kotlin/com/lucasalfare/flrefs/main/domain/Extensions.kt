package com.lucasalfare.flrefs.main.domain

import java.text.Normalizer

/**
 * Custom function to recursively get the root cause of a [Throwable].
 *
 * This function is used to retrieve the root cause of an exception,
 * avoiding the use of Ktor API functions marked with [@InternalApi].
 *
 * @return The root cause [Throwable].
 */
fun Throwable.customRootCause(): Throwable =
  if (cause == null) this else cause!!.customRootCause()

/**
 * Removes accentuation marks from the string.
 *
 * @return A new string with accentuation marks removed.
 */
fun String.removeAccentuation() =
  Normalizer.normalize(
    this,
    Normalizer.Form.NFD
  ).replace(
    regex = Regex("\\p{InCombiningDiacriticalMarks}+"),
    replacement = ""
  )