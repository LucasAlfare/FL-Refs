@file:Suppress("PropertyName", "SpellCheckingInspection")

val ktor_version: String by project
val exposed_version: String by project

plugins {
  kotlin("jvm")
  id("org.jetbrains.kotlin.plugin.serialization")
//  kotlin("plugin.serialization")
}

group = "com.lucasalfare.flrefs"
version = "1.0"

dependencies {
  // ktor client
  api("io.ktor:ktor-client-core:$ktor_version")
  api("io.ktor:ktor-client-cio:$ktor_version")

  // Client Serialization
  api("io.ktor:ktor-client-content-negotiation:$ktor_version")
  api("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

  testImplementation("io.mockk:mockk:1.13.3") // MockK para criar mocks
  testImplementation("io.ktor:ktor-client-mock:$ktor_version") // Mock de cliente HTTP Ktor
//  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4") // Teste de corrotinas
  testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
  jvmToolchain(17)
}