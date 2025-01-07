package my.rudione.plugins

import io.ktor.server.application.Application
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.routing.routing
import my.rudione.route.authRouting
import my.rudione.route.followsRouting
import my.rudione.route.postCommentsRouting
import my.rudione.route.postLikesRouting
import my.rudione.route.postRouting
import my.rudione.route.profileRouting

fun Application.configureRouting() {
    routing {
        authRouting()
        followsRouting()
        postRouting()
        profileRouting()
        postCommentsRouting()
        postLikesRouting()
        static {
            resources("static")
        }
    }
}
