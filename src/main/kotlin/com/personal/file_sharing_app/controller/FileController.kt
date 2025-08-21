package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.service.FileStorageService
import com.personal.file_sharing_app.storage.FileStorage
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/files")
class FileController(
    private val fileStorage : FileStorageService
) {

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") file : MultipartFile,
        @RequestParam("folderId", required = false) folderId : Long?,
    ) : ResponseEntity<File>{

        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        val savedFile = fileStorage.uploadFile(userId, file, folderId)
        return ResponseEntity.ok(savedFile)
    }

    @GetMapping("/{id}")
    fun downloadFile(@PathVariable id: Long): ResponseEntity<Resource> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        val resource = fileStorage.getFile(id, userId)

        val filename = resource.filename ?: "downloaded_file"

        return ResponseEntity.ok()
            // this tells the browser/postman to *download*
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            // try to guess the content type (fallback to octet-stream)
            .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(resource.file.absolutePath)) ?: "application/octet-stream")
            .body(resource)
    }

    @GetMapping
    fun listOfAllFiles() : List<File> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        return fileStorage.listOfFiles(userId)
    }


    @DeleteMapping("/{id}")
    fun deleteFile(@PathVariable id : Long) : ResponseEntity<Void> {

        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        fileStorage.deleteFile(id, userId)
        return ResponseEntity.noContent().build()
    }

}