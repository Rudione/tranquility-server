package my.rudione.util

import io.ktor.http.content.PartData
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import java.io.File
import java.util.UUID

suspend fun PartData.FileItem.saveFile(folderPath: String): String {
    val fileName = "${UUID.randomUUID()}.${File(originalFileName ?: "").extension}"

    val fileBytes = provider().readRemaining().readByteArray()

    val folder = File(folderPath)
    if (!folder.exists()) {
        folder.mkdirs()
    }

    File("$folder/$fileName").writeBytes(fileBytes)

    return fileName
}