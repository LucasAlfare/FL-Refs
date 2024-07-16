package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.Constants
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Object responsible for managing the application's database connection using HikariCP and Exposed.
 *
 * This object provides methods to initialize the database connection, execute queries within transactions,
 * and configure the HikariDataSource.
 *
 * TODO: remove specific Exposed impl code to dedicated abstraction
 */
@Suppress("MemberVisibilityCanBePrivate")
object AppDB {

  /**
   * The HikariDataSource instance used for database connections.
   * Initialized during the `initialize()` method call.
   */
  private lateinit var hikariDataSource: HikariDataSource

  /**
   * The Exposed Database instance, lazily initialized using the HikariDataSource.
   */
  internal val DB by lazy { Database.connect(hikariDataSource) }

  /**
   * Initializes the database connection using the provided parameters.
   * Opens a transaction to ensure the database connection is valid and executes a callback.
   *
   * @param jdbcUrl The JDBC URL for the database connection.
   * @param jdbcDriverClassName The class name of the JDBC driver.
   * @param username The username for the database connection.
   * @param password The password for the database connection.
   * @param onFirstTransactionCallback A callback function executed during the first transaction.
   */
  fun initialize(
    jdbcUrl: String,
    jdbcDriverClassName: String,
    username: String,
    password: String,
    maximumPoolSize: Int,
    onFirstTransactionCallback: () -> Unit = {}
  ) {
    // Creating HikariDataSource using provided parameters.
    hikariDataSource = createHikariDataSource(
      jdbcUrl = jdbcUrl,
      jdbcDriverClassName = jdbcDriverClassName,
      username = username,
      password = password,
      maximumPoolSize = maximumPoolSize
    )

    // Opening a transaction to ensure the database connection is valid.
    transaction(DB) {
      onFirstTransactionCallback()
    }
  }

  /**
   * Executes a database query within a suspended transaction, using the provided query code block.
   *
   * @param T The type of the result returned by the query.
   * @param queryCodeBlock The suspendable code block containing the query to be executed.
   * @return The result of the query execution.
   */
  suspend fun <T> exposedQuery(queryCodeBlock: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = DB) {
      queryCodeBlock()
    }

  /**
   * Creates and configures a HikariDataSource using the provided parameters.
   *
   * @param jdbcUrl The JDBC URL for the database connection.
   * @param jdbcDriverClassName The class name of the JDBC driver.
   * @param username The username for the database connection.
   * @param password The password for the database connection.
   * @return The configured HikariDataSource instance.
   */
  private fun createHikariDataSource(
    jdbcUrl: String,
    jdbcDriverClassName: String,
    username: String,
    password: String,
    maximumPoolSize: Int,
  ): HikariDataSource {
    // Configuring HikariCP with provided parameters.
    val hikariConfig = HikariConfig().apply {
      if (jdbcDriverClassName == Constants.SQLITE_DRIVER)
        this.transactionIsolation = IsolationLevel.TRANSACTION_SERIALIZABLE.name

      this.jdbcUrl = jdbcUrl
      this.driverClassName = jdbcDriverClassName
      this.username = username
      this.password = password
      this.maximumPoolSize = maximumPoolSize
      this.isAutoCommit = true
      this.validate()
    }

    // Creating and returning HikariDataSource.
    return HikariDataSource(hikariConfig)
  }
}