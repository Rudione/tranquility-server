package my.rudione.dao

import my.rudione.dao.follows.FollowsTable
import my.rudione.dao.post.PostTable
import my.rudione.dao.post_comments.PostCommentsTable
import my.rudione.dao.post_likes.PostLikesTable
import my.rudione.dao.user.UserRow
import my.rudione.dao.user.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(){
        Database.connect(createHikariDataSource())
        transaction {
            SchemaUtils.create(UserTable, FollowsTable, PostTable, PostLikesTable, PostCommentsTable)
        }
    }

    private fun createHikariDataSource(): HikariDataSource{
        val driverClass = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql://localhost:5432/tranquility"
        val username = System.getenv("DB_USER")
        val password = System.getenv("DB_PASSWORD")

        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            setUsername(username)
            setPassword(password)
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: suspend () -> T) =
        newSuspendedTransaction(Dispatchers.IO) { block()  }
}