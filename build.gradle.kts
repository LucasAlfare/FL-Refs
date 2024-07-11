group = "com.com.lucasalfare.flrefs"
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

//@file:Suppress("PropertyName", "SpellCheckingInspection")
//
//val ktor_version: String by project
//val exposed_version: String by project
//val thumbnailator_version: String by project
//
//plugins {
//  kotlin("jvm") version "2.0.0"
//  id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
//  application
//}
//
//group = "com.com.lucasalfare.flrefs"
//version = "1.0"
//
//repositories {
//  mavenCentral()
//}
//
//dependencies {
//
//  // Ktor (base and engine)
//  implementation("io.ktor:ktor-server-core:$ktor_version")
//  implementation("io.ktor:ktor-server-netty:$ktor_version")
//
//  // Serialization
//  implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
//  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
//
//  // CORS...
//  implementation("io.ktor:ktor-server-cors:$ktor_version")
//
//  // StatusPages plugin
//  implementation("io.ktor:ktor-server-status-pages:$ktor_version")
//
//  // Cryptography
//  implementation("org.mindrot:jbcrypt:0.4")
//
//  // SQL Framework Exposed Core, JDBC transport layer and date module
//  implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
//  implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
//  implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.52.0")
//
//  /*
//  // SQLite dependency
//  Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
//  TransactionManager
//    .manager
//    .defaultIsolationLevel = Connection
//      .TRANSACTION_SERIALIZABLE
//   */
//  implementation("org.xerial:sqlite-jdbc:3.45.2.0")
//
//  // Postgres dependency
//  implementation("org.postgresql:postgresql:42.7.3")
//
//  // HikariCP (ConnectionPool)
//  implementation("com.zaxxer:HikariCP:5.1.0")
//
//  implementation("net.coobird:thumbnailator:$thumbnailator_version")
//  implementation("ch.qos.logback:logback-classic:1.5.3")
//
//  // ktor client
//  implementation("io.ktor:ktor-client-core:$ktor_version")
//  implementation("io.ktor:ktor-client-cio:$ktor_version")
//
//  // Client Serialization
//  implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
//
//  testImplementation("org.jetbrains.kotlin:kotlin-test")
//}
//
//tasks.test {
//  useJUnitPlatform()
//}
//
//kotlin {
//  jvmToolchain(17)
//}
//
//application {
//  // Define the main class for the application.
//  mainClass.set("com.com.lucasalfare.flrefs.main.MainKt")
//}
//
///*
//This specifies a custom task for creating a ".jar" for this project.
//The main thing is to define manifest and include all dependencies in the final `.jar`.
//
//Also, this is needed because we need to specify that info when creating a jar.
// */
//tasks.withType<Jar> {
//  manifest {
//    // "Main-Class" is set to the actual main file path
//    attributes["Main-Class"] = "com.com.lucasalfare.flrefs.main.MainKt"
//  }
//
//  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//  from(configurations.compileClasspath.map { config -> config.map { if (it.isDirectory) it else zipTree(it) } })
//}