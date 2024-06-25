@file:Suppress("PropertyName", "SpellCheckingInspection")

val kotlin_version: String by project
val ktor_version: String by project

plugins {
  kotlin("jvm") version "1.9.23"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
  application
}

group = "com.lucasalfare"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
//  // dependências do Ktor (core e motor de fundo)
//  implementation("io.ktor:ktor-server-core:$ktor_version")
//  implementation("io.ktor:ktor-server-netty:$ktor_version")
//
//  // dependências para habilitar serialização
//  implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
//  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
//
//  // dependências para gerenciamento de JWT
//  implementation("io.ktor:ktor-server-auth:$ktor_version")
//  implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
//
//  // por algum motivo JWT está requerindo essa dependência
//  implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
//
//  // CORS...
//  implementation("io.ktor:ktor-server-cors:$ktor_version")
//
//  // StatusPages plugin
//  implementation("io.ktor:ktor-server-status-pages:$ktor_version")
//
//  // dependência para criptografar a senha
//  implementation("org.mindrot:jbcrypt:0.4")
//
//  // Dependencies for database manipulation
//  implementation("org.jetbrains.exposed:exposed-core:0.48.0")
//
//  /*
//  // SQLite dependencies
//  Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
//  TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
//   */
//  implementation("org.xerial:sqlite-jdbc:3.45.2.0")
//
//  /*
//  // Postgres dependency
//   */
//  implementation("org.postgresql:postgresql:42.7.3")
//
//  implementation("com.zaxxer:HikariCP:5.1.0")
//
//  // dependência para Firebase Admin
////    implementation("com.google.firebase:firebase-admin:9.2.0")
//
//  // isso aqui serve apenas para gerar os logs da engine do servidor...
//  implementation("ch.qos.logback:logback-classic:1.5.3")

  implementation("com.lucasalfare.flbase:FL-Base") {
    version {
      branch = "main" // convenience to target the branch "main"
    }
  }

  // dependências para contentnegotiation para CLIENT
  testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

  testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
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