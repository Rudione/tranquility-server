package my.rudione.dao.user

import my.rudione.model.SignUpParams
import my.rudione.model.User

interface UserDao {
    suspend fun insert(params: SignUpParams): User?
    suspend fun findUserByEmail(email: String): User?
}