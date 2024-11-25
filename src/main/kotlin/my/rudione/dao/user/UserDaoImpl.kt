package my.rudione.dao.user

import my.rudione.dao.DatabaseFactory.dbQuery
import my.rudione.model.SignUpParams
import my.rudione.model.User
import my.rudione.model.UserRow
import my.rudione.security.hashPassword
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): User? {
        return dbQuery {
            val insertStatement = UserRow.insert {
                it[name] = params.name
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }

            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }
    }

    override suspend fun findUserByEmail(email: String): User? {
        return dbQuery {
            UserRow.selectAll().where { UserRow.email eq email }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[UserRow.id],
            name = row[UserRow.name],
            bio = row[UserRow.bio],
            password = row[UserRow.password],
            avatar = row[UserRow.avatar]
        )
    }
}