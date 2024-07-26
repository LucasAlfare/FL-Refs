package com.lucasalfare.flrefs.main.domain

/**
 * Represents a dynamically loaded environment variable with fallbacks and error handling.
 *
 * @property name The name of the environment variable.
 * @property defaultWhenNull The default value to use when the environment variable is null.
 * @property defaultWhenEmpty The default value to use when the environment variable is empty.
 * @property throwWhenNull Flag indicating whether to throw an exception when the environment variable is null.
 * @property throwWhenEmpty Flag indicating whether to throw an exception when the environment variable is empty.
 */
data class EnvValue(
  private val name: String,
  private val defaultWhenNull: String,
  private val defaultWhenEmpty: String,
  private val throwWhenNull: Boolean = false,
  private val throwWhenEmpty: Boolean = false
) {
  private val value = System.getenv(name).let {
    when {
      it == null -> {
        if (throwWhenNull)
          throw NullEnvironmentVariable("Server environment variable [$name] is missing/null")
        defaultWhenNull
      }

      it.isEmpty() -> {
        if (throwWhenEmpty)
          throw EmptyEnvironmentVariable("Server environment variable [$name] is missing/empty")
        defaultWhenEmpty
      }

      else -> it
    }
  }

  /**
   * Returns the string representation of the loaded environment value.
   *
   * @return The environment value as a string.
   */
  override fun toString() = value
}

/**
 * Utility object to load commonly used environment variables.
 */
object EnvsLoader {
  // Commonly used environment variables
  internal val driverClassNameEnv = EnvValue(
    "DB_JDBC_DRIVER",
    Constants.SQLITE_DRIVER,
    Constants.SQLITE_DRIVER
  ).toString()

  internal val databaseUrlEnv = EnvValue(
    "DB_JDBC_URL",
    Constants.SQLITE_URL,
    Constants.SQLITE_URL
  ).toString()

  internal val databaseUsernameEnv = EnvValue("DB_USERNAME", "", "").toString()
  internal val databasePasswordEnv = EnvValue("DB_PASSWORD", "", "").toString()

  internal val databasePoolSizeEnv = EnvValue(
    "DB_POOL_SIZE",
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString(),
    Constants.DEFAULT_MAXIMUM_POOL_SIZE.toString()
  ).toString()

  internal val webServerPort = EnvValue(
    "WEB_SERVER_PORT",
    Constants.DEFAULT_WEB_SERVER_PORT.toString(),
    Constants.DEFAULT_WEB_SERVER_PORT.toString()
  ).toString()

  /**
   * Loads an environment variable with optional default and error handling configuration.
   *
   * @param name The name of the environment variable to load.
   * @param defaultWhenNull The default value to use when the environment variable is null.
   * @param defaultWhenEmpty The default value to use when the environment variable is empty.
   * @param throwWhenNull Flag indicating whether to throw an exception when the environment variable is null.
   * @param throwWhenEmpty Flag indicating whether to throw an exception when the environment variable is empty.
   * @return The loaded environment variable value as a string.
   */
  fun loadEnv(
    name: String,
    defaultWhenNull: String = "",
    defaultWhenEmpty: String = "",
    throwWhenNull: Boolean = true,
    throwWhenEmpty: Boolean = true
  ) = EnvValue(
    name, defaultWhenNull, defaultWhenEmpty, throwWhenNull, throwWhenEmpty
  ).toString()
}