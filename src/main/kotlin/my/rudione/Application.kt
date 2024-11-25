package my.rudione

import io.ktor.server.application.*
import my.rudione.dao.DatabaseFactory
import my.rudione.di.configureDI
import my.rudione.plugins.configureRouting
import my.rudione.plugins.configureSecurity
import my.rudione.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureDI()
    configureSerialization()
    configureSecurity()
    configureRouting()
}