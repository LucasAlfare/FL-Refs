package com.lucasalfare.flrefs.main

import java.text.Normalizer

fun String.removeAccentuation() =
  Normalizer.normalize(
    this,
    Normalizer.Form.NFD
  ).replace(
    regex = Regex("\\p{InCombiningDiacriticalMarks}+"),
    replacement = ""
  )