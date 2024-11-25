package my.rudione.route

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import my.rudione.model.AuthResponse
import my.rudione.model.SignInParams
import my.rudione.model.SignUpParams
import my.rudione.repository.UserRepository
import org.koin.ktor.ext.inject

fun Routing.authRouting() {
    val repository by inject<UserRepository>()

    route(path = "/signup") {
        post {
            val params = call.receiveNullable<SignUpParams>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Невірні дані"
                    )
                )

                return@post
            }

            val result = repository.signUp(params = params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }

    route(path = "/login") {
        post {
            val params = call.receiveNullable<SignInParams>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Невірні дані"
                    )
                )

                return@post
            }

            val result = repository.signIn(params = params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}