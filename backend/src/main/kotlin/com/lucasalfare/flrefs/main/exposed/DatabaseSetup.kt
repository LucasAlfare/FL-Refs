package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flrefs.main.EnvsLoader.databasePassword
import com.lucasalfare.flrefs.main.EnvsLoader.databasePoolSize
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUrl
import com.lucasalfare.flrefs.main.EnvsLoader.databaseUsername
import com.lucasalfare.flrefs.main.EnvsLoader.driverClassName
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Initializes the database and sets up the specified tables.
 *
 * This function initializes the database connection using environment variables for the
 * JDBC URL, JDBC driver class name, username, password and maximum pool size. It optionally
 * drops tables on startup and then creates missing tables and columns for the provided tables.
 *
 * @param tables The tables to be initialized in the database.
 * @param dropTablesOnStart If true, drops the specified tables before creating them. The default is false.
 *
 * @example
 * ```
 * // Example usage to initialize the database with User and Order tables, dropping them on start
 * initDatabase(UserTable, OrderTable, dropTablesOnStart = true)
 * ```
 */
fun initDatabase(
  vararg tables: Table,
  dropTablesOnStart: Boolean = false
) {
  AppDB.initialize(
    jdbcUrl = databaseUrl,
    jdbcDriverClassName = driverClassName,
    username = databaseUsername,
    password = databasePassword,
    maximumPoolSize = databasePoolSize.toInt()
  ) {
    tables.forEach {
      if (dropTablesOnStart) SchemaUtils.drop(it)
      transaction(AppDB.DB) { SchemaUtils.createMissingTablesAndColumns(it) }
    }
  }
}