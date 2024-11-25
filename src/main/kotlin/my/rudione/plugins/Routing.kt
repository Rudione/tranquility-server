package my.rudione.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import my.rudione.route.authRouting

fun Application.configureRouting() {
    routing {
        authRouting()
    }
}
