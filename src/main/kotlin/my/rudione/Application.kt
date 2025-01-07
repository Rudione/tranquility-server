package my.rudione

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import my.rudione.dao.DatabaseFactory
import my.rudione.di.configureDI
import my.rudione.plugins.configureRouting
import my.rudione.plugins.configureSecurity
import my.rudione.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting()
}
