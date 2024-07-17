@file:Suppress("PropertyName", "SpellCheckingInspection")

val ktor_version: String by project
val exposed_version: String by project
val thumbnailator_version: String by project

plugins {
  kotlin("jvm")
  id("org.jetbrains.kotlin.plugin.serialization")
  application
}

group = "com.lucasalfare.flrefs"
version = "1.0"

dependencies {
  implementation(project(":github-wrapper"))

  // Ktor (base and engine)
  implementation("io.ktor:ktor-server-core:$ktor_version")
  implementation("io.ktor:ktor-server-netty:$ktor_version")

  // Serialization
  implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

  // CORS...
  implementation("io.ktor:ktor-server-cors:$ktor_version")

  // StatusPages plugin
  implementation("io.ktor:ktor-server-status-pages:$ktor_version")

  // Cryptography
  implementation("org.mindrot:jbcrypt:0.4")

  // SQL Framework Exposed Core, JDBC transport layer and date module
  implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
  implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")

  /*
  // SQLite dependency
  Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
  TransactionManager
    .manager
    .defaultIsolationLevel = Connection
      .TRANSACTION_SERIALIZABLE
   */
  implementation("org.xerial:sqlite-jdbc:3.45.2.0")

  // Postgres dependency
  implementation("org.postgresql:postgresql:42.7.3")

  // HikariCP (ConnectionPool)
  implementation("com.zaxxer:HikariCP:5.1.0")

  implementation("net.coobird:thumbnailator:$thumbnailator_version")
  implementation("ch.qos.logback:logback-classic:1.5.3")

  testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
  testImplementation("io.ktor:ktor-client-mock:$ktor_version") // Mock de cliente HTTP Ktor
  testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
  jvmToolchain(17)
}

application {
  // Define the main class for the application.
  mainClass.set("com.lucasalfare.flrefs.main.MainKt")
}

tasks.withType<Jar> {
  manifest {
    // "Main-Class" is set to the actual main file path
    attributes["Main-Class"] = application.mainClass
  }

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  from(configurations.compileClasspath.map { config -> config.map { if (it.isDirectory) it else zipTree(it) } })
}