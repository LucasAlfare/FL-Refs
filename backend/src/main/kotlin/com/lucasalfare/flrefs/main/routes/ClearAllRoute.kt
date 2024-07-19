package com.lucasalfare.flrefs.main.routes

import com.lucasalfare.flrefs.main.data.exposed.AppDB
import com.lucasalfare.flrefs.main.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.data.exposed.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Defines a route for clearing all items in the database.
 *
 * This route deletes all entries in the ImagesInfos and ImagesUrls tables.
 *
 * TODO: This route should be placed inside an authentication block to restrict access.
 */
fun Routing.clearAllItemsRoute() {
  // TODO: this MUST be inside auth block!
  delete("/clear") {
    return@delete AppDB.exposedQuery {
      transaction {
        val databaseType = TransactionManager.current().db.dialect.name.lowercase()
        val cascade =
          if (databaseType.contains("PostgreSQL".lowercase())) " CASCADE"
          else ""

        SchemaUtils.listTables().forEach {
          exec("DROP TABLE IF EXISTS $it$cascade")
        }

        SchemaUtils.createMissingTablesAndColumns(
          Users,
          ImagesInfos,
          ImagesUrls
        )
      }
      // TODO: include call to CDN clear
      call.respond(HttpStatusCode.OK, "All tables were cleared.")
    }
  }
}