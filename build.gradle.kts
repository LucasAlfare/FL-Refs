@file:Suppress("SpellCheckingInspection")

group = "com.lucasalfare.flrefs"
version = "1.0"

plugins {
  kotlin("jvm") apply false
  id("org.jetbrains.kotlin.plugin.serialization") apply false
}

allprojects {
  repositories {
    mavenCentral()
  }
}