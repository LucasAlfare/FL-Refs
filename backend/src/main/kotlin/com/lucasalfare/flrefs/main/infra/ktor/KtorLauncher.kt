package com.lucasalfare.flrefs.main.infra.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*

object KtorLauncher {

  fun start() {
    // Start embedded server
    embeddedServer(Netty, port = 80) {
      configureAuthentication()
      configureCORS()
      configureSerialization()
      configureStatusPages()
      configureLargePayloadRejector()
      configureRouting()
    }.start(wait = true)
  }
}