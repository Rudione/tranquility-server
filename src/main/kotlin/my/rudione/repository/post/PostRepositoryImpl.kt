package my.rudione.repository.post

import my.rudione.dao.follows.FollowsDao
import my.rudione.dao.post.PostDao
import my.rudione.dao.post.PostRow
import my.rudione.dao.post_likes.PostLikesDao
import my.rudione.model.Post
import my.rudione.model.PostResponse
import my.rudione.model.PostTextParams
import my.rudione.model.PostsResponse
import my.rudione.util.Response
import io.ktor.http.*

class PostRepositoryImpl(
    private val postDao: PostDao,
    private val followsDao: FollowsDao,
    private val postLikesDao: PostLikesDao
) : PostRepository {
    override suspend fun createPost(imageUrl: String, postTextParams: PostTextParams): Response<PostResponse> {
        val postRow = postDao.createPost(
            caption = postTextParams.caption,
            imageUrl = imageUrl,
            userId = postTextParams.userId
        )

        return if (postRow != null) {
            Response.Success(
                data = PostResponse(
                    success = true,
                    post = toPost(
                        postRow = postRow,
                        isPostLiked = false,
                        isOwnPost = true
                    )
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(
                    success = false,
                    message = "Post could not be inserted in the db"
                )
            )
        }

    }

    override suspend fun getFeedPosts(userId: Long, pageNumber: Int, pageSize: Int): Response<PostsResponse> {
        val followingUsers = followsDao.getAllFollowing(userId = userId).toMutableList()
        followingUsers.add(userId)

        val postsRows = postDao.getFeedsPost(
            userId = userId,
            follows = followingUsers,
            pageNumber = pageNumber,
            pageSize = pageSize
        )

        val posts = postsRows.map {
            toPost(
                postRow = it,
                isPostLiked = postLikesDao.isPostLikedByUser(postId = it.postId, userId = userId),
                isOwnPost = it.userId == userId
            )
        }

        return Response.Success(
            data = PostsResponse(
                success = true,
                posts = posts
            )
        )
    }

    override suspend fun getPostsByUser(
        postsOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Response<PostsResponse> {
        val postsRows = postDao.getPostByUser(userId = postsOwnerId, pageNumber = pageNumber, pageSize = pageSize)

        val posts = postsRows.map {
            toPost(
                postRow = it,
                isPostLiked = postLikesDao.isPostLikedByUser(postId = it.postId, userId = currentUserId),
                isOwnPost = it.userId == currentUserId
            )
        }

        return Response.Success(
            data = PostsResponse(
                success = true,
                posts = posts
            )
        )
    }

    override suspend fun getPost(postId: Long, currentUserId: Long): Response<PostResponse> {
        val post = postDao.getPost(postId = postId)

        return if (post == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(success = false, message = "Could not retrieve post from the database")
            )
        } else {
            val isPostLiked = postLikesDao.isPostLikedByUser(postId, currentUserId)
            val isOwnPost = post.postId == currentUserId
            Response.Success(
                data = PostResponse(success = true, toPost(post, isPostLiked = isPostLiked, isOwnPost = isOwnPost))
            )
        }
    }

    override suspend fun deletePost(postId: Long): Response<PostResponse> {
        val postIsDeleted = postDao.deletePost(
            postId = postId
        )

        return if (postIsDeleted) {
            Response.Success(
                data = PostResponse(success = true)
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(
                    success = false,
                    message = "Post could not be deleted from the db"
                )
            )
        }
    }

    private fun toPost(postRow: PostRow, isPostLiked: Boolean, isOwnPost: Boolean): Post {
        return Post(
            postId = postRow.postId,
            caption = postRow.caption,
            imageUrl = postRow.imageUrl,
            createdAt = postRow.createdAt,
            likesCount = postRow.likesCount,
            commentsCount = postRow.commentsCount,
            userId = postRow.userId,
            userImageUrl = postRow.userImageUrl,
            userName = postRow.userName,
            isLiked = isPostLiked,
            isOwnPost = isOwnPost
        )
    }
}