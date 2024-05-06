package com.lucasalfare.flrefs.main


class Constants {
  companion object {
    @Deprecated("Postgres URL are now handled by Environment variables.")
    const val POSTGRES_URL = "jdbc:postgresql://TTTT:0000/"

    @Deprecated("Postgres URL are now handled by Environment variables.")
    const val POSTGRES_DRIVER = "org.postgresql.Driver"

    const val SQLITE_URL = "jdbc:sqlite:./data.db"
    const val SQLITE_DRIVER = "org.sqlite.JDBC"
  }
}