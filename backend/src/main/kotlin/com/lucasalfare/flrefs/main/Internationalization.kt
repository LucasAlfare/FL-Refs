package com.lucasalfare.flrefs.main

data class LangGroup(
  val values: Map<String, String>
) {
  operator fun get(str: String) = try {
    values[str]!!
  } catch (e: Exception) {
    throw Throwable("can not to retrieve language group [$str]")
  }
}

data class Langs(
  val groups: Map<String, LangGroup>
) {
  operator fun get(str: String) = try {
    groups[str]!!.values
  } catch (e: Exception) {
    throw Throwable("can not to retrieve language group [$str]")
  }
}

// @formatter:off
fun setupExample() {
  val langs = Langs(
    mapOf(
      Pair("pt-BR", LangGroup(mapOf(
        Pair("ENV_VAR", "valor bom"),
        Pair("OTHER_VAR", "outra variável boa")
      ))),

      Pair("en-US", LangGroup(mapOf(
        Pair("ENV_VAR", "nice value"),
        Pair("OTHER_VAR", "other nice variable")
      )))
    )
  )

  var currentLang = "pt-BR"
  println(langs[currentLang]["ENV_VAR"])

  currentLang = "en"
  println(langs[currentLang]["ENV_VAR"])
}
// @formatter:on