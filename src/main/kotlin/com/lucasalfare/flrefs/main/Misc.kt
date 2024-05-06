package com.lucasalfare.flrefs.main


class Constants {
  companion object {
    // fields just to quick change between databases
    const val POSTGRES_URL = "jdbc:postgresql://TTTT:0000/"
    const val POSTGRES_DRIVER = "org.postgresql.Driver"

    const val SQLITE_URL = "jdbc:sqlite:./data.db"
    const val SQLITE_DRIVER = "org.sqlite.JDBC"
  }
}