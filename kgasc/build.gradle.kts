@file:Suppress("PropertyName", "SpellCheckingInspection")

val ktor_version: String by project
val exposed_version: String by project

plugins {
  kotlin("jvm")
  id("org.jetbrains.kotlin.plugin.serialization")
}

group = "com.com.lucasalfare.flrefs"
version = "1.0"

dependencies {
  // ktor client
  implementation("io.ktor:ktor-client-core:$ktor_version")
  implementation("io.ktor:ktor-client-cio:$ktor_version")

  // Client Serialization
  implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
}