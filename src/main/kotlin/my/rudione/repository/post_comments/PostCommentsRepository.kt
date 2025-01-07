package my.rudione.repository.post_comments

import my.rudione.model.CommentResponse
import my.rudione.model.GetCommentsResponse
import my.rudione.model.NewCommentParams
import my.rudione.model.RemoveCommentParams
import my.rudione.util.Response

interface PostCommentsRepository {
    suspend fun addComment(params: NewCommentParams): Response<CommentResponse>

    suspend fun removeComment(params: RemoveCommentParams): Response<CommentResponse>

    suspend fun getPostComments(postId: Long, pageNumber: Int, pageSize: Int): Response<GetCommentsResponse>
}