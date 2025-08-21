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


@Service
class LocalStorageService(
    @Value("\${app.upload-dir}") private val uploadDir : String
) : StorageService{

    override fun save(file: MultipartFile): String {

        val uniqueName = "${System.currentTimeMillis()}_${file.originalFilename}"
        val targetPath = Paths.get(uploadDir).resolve(uniqueName)

        Files.createDirectories(targetPath.parent)
        file.inputStream.use { input -> Files.copy(
            input,
            targetPath,
            StandardCopyOption.REPLACE_EXISTING
        )}

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

}