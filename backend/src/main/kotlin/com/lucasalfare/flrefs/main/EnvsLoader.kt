package com.lucasalfare.flrefs.main

data class EnvValue(
  private val name: String,
  private val defaultWhenNull: String,
  private val defaultWhenEmpty: String,
  private val throwWhenNull: Boolean = false,
  private val throwWhenEmpty: Boolean = false
) {
  private val value = System.getenv(name).let {
    if (it == null) {
      if (throwWhenNull)
        throw NullEnvironmentVariable("Server environment variable [$name] is missing/null")
      defaultWhenNull
    } else if (it.isEmpty()) {
      if (throwWhenEmpty)
        throw EmptyEnvironmentVariable("Server environment variable [$name] is missing/empty")
      defaultWhenEmpty
    } else {
      it
    }
  }

  override fun toString() = value
}

object EnvsLoader {
  // loads common used environments
  internal val driverClassName = EnvValue(
    "DB_JDBC_DRIVER",
    Constants.SQLITE_DRIVER,
    Constants.SQLITE_DRIVER
  ).toString()
  internal val databaseUrl = EnvValue(
    "DB_JDBC_URL",
    Constants.SQLITE_URL,
    Constants.SQLITE_URL
  ).toString()
  internal val databaseUsername = EnvValue("DB_USERNAME", "", "").toString()
  internal val databasePassword = EnvValue("DB_PASSWORD", "", "").toString()
  internal val databasePoolSize = EnvValue(
    "DB_POOL_SIZE",
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString(),
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString()
  ).toString()
  internal val webServerPort = EnvValue(
    "WEB_SERVER_PORT",
    Constants.DEFAULT_WEB_SERVER_PORT.toString(),
    Constants.DEFAULT_WEB_SERVER_PORT.toString()
  ).toString()

  fun customLoad(
    name: String,
    defaultWhenNull: String,
    defaultWhenEmpty: String,
    throwWhenNull: Boolean = false,
    throwWhenEmpty: Boolean = false
  ) = EnvValue(
    name, defaultWhenNull, defaultWhenEmpty, throwWhenNull, throwWhenEmpty
  ).toString()
}