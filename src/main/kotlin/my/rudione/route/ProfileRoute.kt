package my.rudione.route

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json
import my.rudione.model.ProfileResponse
import my.rudione.model.UpdateUserParams
import my.rudione.repository.profile.ProfileRepository
import my.rudione.util.Constants
import my.rudione.util.getLongParameter
import my.rudione.util.saveFile
import org.koin.ktor.ext.inject
import java.io.File

fun Routing.profileRouting(){
    val repository by inject<ProfileRepository>()

    authenticate {

        route(path = "/profile"){

            get(path = "/{userId}"){
                try {
                    val profileOwnerId = call.getLongParameter(name = "userId")
                    val currentUserId = call.getLongParameter(name = "currentUserId", isQueryParameter = true)

                    val result = repository.getUserById(userId = profileOwnerId, currentUserId = currentUserId)
                    call.respond(status = result.code, message = result.data)
                }catch (badRequestError: BadRequestException){
                    return@get
                }catch (anyError: Throwable){
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = ProfileResponse(
                            success = false,
                            message = Constants.UNEXPECTED_ERROR_MESSAGE
                        )
                    )
                }
            }

            post(path = "/update"){
                var fileName = ""
                var updateUserParams: UpdateUserParams? = null
                val multiPartData = call.receiveMultipart()

                try {
                    multiPartData.forEachPart { partData ->
                        when(partData){
                            is PartData.FileItem -> {
                                fileName = partData.saveFile(folderPath = Constants.PROFILE_IMAGES_FOLDER_PATH)
                            }
                            is PartData.FormItem -> {
                                if (partData.name == "profile_data"){
                                    updateUserParams = Json.decodeFromString(partData.value)
                                }
                            }
                            else -> {}
                        }
                        partData.dispose()
                    }

                    val imageUrl = "${Constants.BASE_URL}${Constants.PROFILE_IMAGES_FOLDER}$fileName"

                    val result = repository.updateUser(
                        updateUserParams = updateUserParams!!.copy(
                            imageUrl = if (fileName.isNotEmpty()) imageUrl else updateUserParams!!.imageUrl
                        )
                    )
                    call.respond(status = result.code, message = result.data)
                }catch (anyError: Throwable){
                    if (fileName.isNotEmpty()){
                        File("${Constants.PROFILE_IMAGES_FOLDER_PATH}/$fileName").delete()
                    }
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = ProfileResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
        }
    }
}















