package my.rudione.repository.post_likes

import my.rudione.model.LikeParams
import my.rudione.model.LikeResponse
import my.rudione.util.Response

interface PostLikesRepository {
    suspend fun addLike(params: LikeParams): Response<LikeResponse>

    suspend fun removeLike(params: LikeParams): Response<LikeResponse>
}