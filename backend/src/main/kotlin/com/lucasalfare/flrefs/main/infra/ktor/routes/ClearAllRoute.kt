package com.lucasalfare.flrefs.main.infra.ktor.routes

import com.lucasalfare.flrefs.main.domain.EnvsLoader
import com.lucasalfare.flrefs.main.domain.localization.Message
import com.lucasalfare.flrefs.main.infra.data.exposed.AppDB
import com.lucasalfare.flrefs.main.infra.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.infra.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.infra.data.exposed.Users
import com.lucasalfare.flrefs.main.usecase.UserServices
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
fun Route.clearAllItemsRoute() {
  delete("/clear") {
    return@delete AppDB.exposedQuery {
      transaction {
        val databaseType = TransactionManager.current().db.dialect.name.lowercase()
        val cascade =
          if (databaseType.contains("PostgreSQL".lowercase())) " CASCADE"
          else ""

        SchemaUtils.listTables().forEach {
          if (it != Users.tableName) {
            exec("DROP TABLE IF EXISTS $it$cascade")
          }
        }

        SchemaUtils.createMissingTablesAndColumns(
          Users,
          ImagesInfos,
          ImagesUrls
        )
      }

      runCatching {
        UserServices.create(
          email = EnvsLoader.loadEnv("ADMIN_EMAIL"),
          plainPassword = EnvsLoader.loadEnv("ADMIN_PLAIN_PASSWORD")
        )
      }

      // TODO: include call to CDN clear
      call.respond(HttpStatusCode.OK, Message.DB_CLEAR_SUCCESS.toString())
    }
  }
}