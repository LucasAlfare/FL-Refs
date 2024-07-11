package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.EnvsLoader.webServerPort
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * Starts a web server using the Netty engine.
 *
 * This function creates and starts an embedded web server on the specified port,
 * applying any additional setup defined in the provided callback.
 *
 * @param port The port number on which the server will listen. The default is `3000`.
 * @param setupCallback A lambda function to define custom setup logic for the server application.
 *
 * @example
 * ```
 * // Example usage to start the server with a custom setup
 * startWebServer(port = 8080) {
 *     configureRouting {
 *         get("/") {
 *             call.respondText("Hello, World!")
 *         }
 *     }
 * }
 * ```
 */
fun startWebServer(
  port: Int = webServerPort.toInt(),
  setupCallback: Application.() -> Unit = {}
) {
  embeddedServer(factory = Netty, port = port) {
    setupCallback()
  }.start(true)
}