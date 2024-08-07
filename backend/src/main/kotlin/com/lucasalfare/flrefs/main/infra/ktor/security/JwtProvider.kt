package com.lucasalfare.flrefs.main.infra.ktor.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.lucasalfare.flrefs.main.domain.EnvsLoader.loadEnv
import io.ktor.server.application.*
import java.util.*

object JwtProvider {

  private val jwtAlgorithmSignSecret =
    loadEnv("JWT_ALGORITHM_SECRET")

  internal val verifier = JWT
    .require(Algorithm.HMAC256(jwtAlgorithmSignSecret))
    .build()

  /**
   * Generated a JWT token based on the inputs stored in the [environment] definitions.
   * Also, can receive a custom [targetClaim] to be included in the final token.
   */
  fun generateJWT(
    targetClaim: String // login/email
  ): String =
    JWT.create()
//      .withExpiresAt(Date(System.currentTimeMillis() + (60000 * 10)))
      .withClaim("email", targetClaim)
      .sign(Algorithm.HMAC256(jwtAlgorithmSignSecret))
}