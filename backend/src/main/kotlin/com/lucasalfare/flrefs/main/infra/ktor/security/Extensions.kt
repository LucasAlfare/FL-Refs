package com.lucasalfare.flrefs.main.infra.ktor.security

import org.mindrot.jbcrypt.BCrypt

// obtain the hashed version of the current String value
fun String.hashed(): String =
  BCrypt.hashpw(this, BCrypt.gensalt())

// checks if the current String values matches a hashed String
fun String.matchHashed(hashed: String) =
  BCrypt.checkpw(this, hashed)