package my.rudione.repository.auth

import my.rudione.model.AuthResponse
import my.rudione.model.SignInParams
import my.rudione.model.SignUpParams
import my.rudione.util.Response

interface AuthRepository {
    suspend fun signUp(params: SignUpParams): Response<AuthResponse>
    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}