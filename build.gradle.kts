@file:Suppress("PropertyName", "SpellCheckingInspection")

plugins {
  kotlin("jvm") version "2.0.0"
  id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
  application
}

group = "com.lucasalfare"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  // All server/database/patterns stuff comes from this template
  implementation("com.lucasalfare.flbase:FL-Base") {
    version {
      branch = "main" // convenience to target the branch "main"
    }
  }

  testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}

application {
  // Define the main class for the application.
  mainClass.set("com.lucasalfare.flrefs.main.MainKt")
}

/*
This specifies a custom task for creating a ".jar" for this project.
The main thing is to define manifest and include all dependencies in the final `.jar`.

Also, this is needed because we need to specify that info when creating a jar.
 */
tasks.withType<Jar> {
  manifest {
    // "Main-Class" is set to the actual main file path
    attributes["Main-Class"] = "com.lucasalfare.flrefs.main.MainKt"
  }

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  from(configurations.compileClasspath.map { config -> config.map { if (it.isDirectory) it else zipTree(it) } })
}