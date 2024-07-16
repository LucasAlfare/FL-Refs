rootProject.name = "FL-Refs"

include(
  ":kgasc",
  ":backend"
)

pluginManagement {
  repositories {
    mavenCentral()
  }

  plugins {
    val kotlinVersion = extra["kotlin_version"] as String

    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
  }
}