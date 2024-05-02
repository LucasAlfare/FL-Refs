// fields just to quick change between databases
const val POSTGRES_URL = "jdbc:postgresql://ola-flyio-pg.internal:5432/"
const val POSTGRES_DRIVER = "org.postgresql.Driver"
const val SQLITE_URL = "jdbc:sqlite:./data.db"
const val SQLITE_DRIVER = "org.sqlite.JDBC"

//const val ONE_MILLISECOND = 1L
//const val ONE_SECOND = 1000 * ONE_MILLISECOND
//const val ONE_MINUTE = 60 * ONE_SECOND
//const val ONE_HOUR = 60 * ONE_MINUTE
//const val ONE_DAY = 24 * ONE_HOUR