package my.rudione.repository.profile

import my.rudione.model.ProfileResponse
import my.rudione.model.UpdateUserParams
import my.rudione.util.Response

interface ProfileRepository {

    suspend fun getUserById(userId: Long, currentUserId: Long): Response<ProfileResponse>

    suspend fun updateUser(updateUserParams: UpdateUserParams): Response<ProfileResponse>
}