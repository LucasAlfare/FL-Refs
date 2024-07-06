package com.lucasalfare.flrefs.main.exposed

import com.lucasalfare.flbase.UnavailableDatabaseService
import com.lucasalfare.flbase.database.AppDB
import com.lucasalfare.flrefs.main.AppServiceAdapter
import org.jetbrains.exposed.sql.insert

object CreateImageRegistryService : AppServiceAdapter() {

  override suspend fun createImageRegistry(name: String, downloadUrl: String) = AppDB.exposedQuery {
    try {
      Images.insert {
        it[Images.name] = name
        it[Images.downloadUrl] = downloadUrl
      }
      Unit
    } catch (e: Exception) {
      throw UnavailableDatabaseService()
    }
  }
}