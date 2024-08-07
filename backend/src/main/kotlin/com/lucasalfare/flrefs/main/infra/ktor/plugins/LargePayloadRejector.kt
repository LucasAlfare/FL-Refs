package com.lucasalfare.flrefs.main.infra.ktor.plugins

import com.lucasalfare.flrefs.main.domain.localization.Message
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*

/**
 * Plugin to reject requests with payloads larger than a specified size.
 */
class LargePayloadRejector {

  /**
   * Configuration class for the LargePayloadRejector plugin.
   */
  class LargePayloadRejectorConfiguration

  companion object Plugin : BaseApplicationPlugin<
          ApplicationCallPipeline,
          LargePayloadRejectorConfiguration,
          LargePayloadRejector> {

    override val key = AttributeKey<LargePayloadRejector>("LargePayloadRejector")

    /**
     * Installs the LargePayloadRejector plugin into the Ktor pipeline.
     *
     * @param pipeline The application call pipeline.
     * @param configure The configuration function for the plugin.
     * @return An instance of LargePayloadRejector.
     */
    override fun install(
      pipeline: ApplicationCallPipeline,
      configure: LargePayloadRejectorConfiguration.() -> Unit
    ): LargePayloadRejector {
      val plugin = LargePayloadRejector()

      pipeline.intercept(ApplicationCallPipeline.Plugins) {
        val contentLength = call.request.header(HttpHeaders.ContentLength)?.toLong()
        if (contentLength != null && contentLength > 10_000 * 1000) {
          call.respond(
            HttpStatusCode.PayloadTooLarge,
            Message.LARGE_PAYLOAD_ERROR.toString()
          )
          finish()
        }
      }

      return plugin
    }
  }
}