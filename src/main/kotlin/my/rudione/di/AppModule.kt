package my.rudione.di

import io.ktor.server.application.Application
import io.ktor.server.application.install
import my.rudione.dao.user.UserDao
import my.rudione.dao.user.UserDaoImpl
import my.rudione.repository.UserRepository
import my.rudione.repository.UserRepositoryImpl
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
}