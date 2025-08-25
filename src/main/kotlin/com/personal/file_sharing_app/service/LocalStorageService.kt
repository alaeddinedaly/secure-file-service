package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.storage.StorageService
import org.springframework.core.io.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.exists
import kotlin.io.path.isDirectory


@Service
class LocalStorageService(
    @Value("\${app.upload-dir}") private val uploadDir : String
) : StorageService{

    fun getUploadDir() : String = uploadDir

    override fun save(file: MultipartFile, folderName : String?): String {

        val uniqueName = "${System.currentTimeMillis()}_${file.originalFilename}"

        val folderPath = if (folderName != null) {
            Paths.get(uploadDir, folderName)
        } else {
            Paths.get(uploadDir)
        }

        Files.createDirectories(folderPath)

        val targetPath = folderPath.resolve(uniqueName)

        file.inputStream.use { input ->
            Files.copy(
                input,
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            )
        }

        return targetPath.toString()
    }

    override fun load(path: String): Resource {
        val resource = UrlResource(Paths.get(path).toUri())
        if (!resource.exists() || !resource.isReadable) {
            throw RuntimeException("File not found or not readable")
        }
        return resource
    }

    override fun delete(path: String) {
        Files.deleteIfExists(Paths.get(path))
    }

    fun createFolder(folderName : String) : String {

        val folderPath = Paths.get(uploadDir, folderName)

        if(folderPath.exists()) {
            throw IllegalArgumentException("Folder already exists : $folderName")
        }

        Files.createDirectories(folderPath)
        return folderPath.toString()

    }
    fun deleteFolder(folderName : String) {

        val folderPath = Paths.get(uploadDir, folderName)

        if(!folderPath.exists() || !folderPath.isDirectory()) {
            throw IllegalArgumentException("Folder not found : $folderName")
        }

        Files.walk(folderPath)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.deleteIfExists(it) }
    }

}