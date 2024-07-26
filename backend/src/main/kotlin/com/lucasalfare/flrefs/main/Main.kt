package com.lucasalfare.flrefs.main

import com.lucasalfare.flrefs.main.domain.EnvsLoader.databasePasswordEnv
import com.lucasalfare.flrefs.main.domain.EnvsLoader.databasePoolSizeEnv
import com.lucasalfare.flrefs.main.domain.EnvsLoader.databaseUrlEnv
import com.lucasalfare.flrefs.main.domain.EnvsLoader.databaseUsernameEnv
import com.lucasalfare.flrefs.main.domain.EnvsLoader.driverClassNameEnv
import com.lucasalfare.flrefs.main.domain.EnvsLoader.loadEnv
import com.lucasalfare.flrefs.main.infra.cdn.github.GithubCdnUploader
import com.lucasalfare.flrefs.main.infra.data.exposed.AppDB
import com.lucasalfare.flrefs.main.infra.data.exposed.ImagesInfos
import com.lucasalfare.flrefs.main.infra.data.exposed.ImagesUrls
import com.lucasalfare.flrefs.main.infra.data.exposed.Users
import com.lucasalfare.flrefs.main.infra.data.exposed.repository.ExposedImagesInfosRepository
import com.lucasalfare.flrefs.main.infra.data.exposed.repository.ExposedImagesUrlsRepository
import com.lucasalfare.flrefs.main.infra.data.exposed.repository.ExposedUsersRepository
import com.lucasalfare.flrefs.main.infra.ktor.KtorLauncher
import com.lucasalfare.flrefs.main.infra.thumbnail.thumbnailator.ThumbnailatorThumbnailGenerator
import com.lucasalfare.flrefs.main.usecase.DataServices
import com.lucasalfare.flrefs.main.usecase.UserServices
import org.jetbrains.exposed.sql.SchemaUtils
import java.util.*

lateinit var currentLocale: Locale

/**
 * Main function to start the application.
 */
suspend fun main() {
  // Initialize database connection and create tables if missing
  initDatabase()
  initOther()

  KtorLauncher.start()
}

fun initDatabase() {

  // TODO: abstract this.
  AppDB.initialize(
    jdbcUrl = databaseUrlEnv,
    jdbcDriverClassName = driverClassNameEnv,
    username = databaseUsernameEnv,
    password = databasePasswordEnv,
    maximumPoolSize = databasePoolSizeEnv.toInt()
  ) {
    SchemaUtils.createMissingTablesAndColumns(
      Users,
      ImagesInfos,
      ImagesUrls
    )
  }
}

internal suspend fun initOther() {
  currentLocale = Locale.ENGLISH

  DataServices.cdnUploader = GithubCdnUploader
  DataServices.imageRepository = ExposedImagesInfosRepository
  DataServices.imageUrlsRepository = ExposedImagesUrlsRepository
  DataServices.thumbnailGenerator = ThumbnailatorThumbnailGenerator
  UserServices.userRepository = ExposedUsersRepository

  // always tries to create admin registry
  runCatching {
    UserServices.create(
      email = loadEnv("ADMIN_EMAIL"),
      plainPassword = loadEnv("ADMIN_PLAIN_PASSWORD")
    )
  }
}