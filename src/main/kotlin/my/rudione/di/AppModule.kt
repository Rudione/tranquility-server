package my.rudione.di

import my.rudione.dao.follows.FollowsDao
import my.rudione.dao.follows.FollowsDaoImpl
import my.rudione.dao.post.PostDao
import my.rudione.dao.post.PostDaoImpl
import my.rudione.dao.post_comments.PostCommentsDao
import my.rudione.dao.post_comments.PostCommentsDaoImpl
import my.rudione.dao.post_likes.PostLikesDao
import my.rudione.dao.post_likes.PostLikesDaoImpl
import my.rudione.dao.user.UserDao
import my.rudione.dao.user.UserDaoImpl
import my.rudione.repository.auth.AuthRepository
import my.rudione.repository.auth.AuthRepositoryImpl
import my.rudione.repository.follows.FollowsRepository
import my.rudione.repository.follows.FollowsRepositoryImpl
import my.rudione.repository.post.PostRepository
import my.rudione.repository.post.PostRepositoryImpl
import my.rudione.repository.post_comments.PostCommentsRepository
import my.rudione.repository.post_comments.PostCommentsRepositoryImpl
import my.rudione.repository.post_likes.PostLikesRepository
import my.rudione.repository.post_likes.PostLikesRepositoryImpl
import my.rudione.repository.profile.ProfileRepository
import my.rudione.repository.profile.ProfileRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao> { FollowsDaoImpl() }
    single<FollowsRepository> { FollowsRepositoryImpl(get(), get()) }
    single<PostLikesDao> { PostLikesDaoImpl() }
    single<PostDao> { PostDaoImpl()}
    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<PostCommentsDao> { PostCommentsDaoImpl() }
    single<PostCommentsRepository> { PostCommentsRepositoryImpl(get(), get()) }
    single<PostLikesRepository> { PostLikesRepositoryImpl(get(), get()) }
}