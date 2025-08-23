package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.dto.ShareFileRequest
import com.personal.file_sharing_app.model.ActionType
import com.personal.file_sharing_app.model.ActivityLog
import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.service.ActivityLogService
import com.personal.file_sharing_app.service.FileAccessService
import com.personal.file_sharing_app.service.FileService
import com.personal.file_sharing_app.service.UserService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/files")
class FileController(
    private val fileStorage : FileService,
    private val activityLogService: ActivityLogService,
    private val userService: UserService,
    private val fileService : FileService,
    private val fileAccessService: FileAccessService
) {

    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") file : MultipartFile,
        @RequestParam("folderId", required = false) folderId : Long?,
    ) : ResponseEntity<File>{

        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val user =  userService.getUserById(userId)

        val savedFile = fileStorage.uploadFile(userId, file, folderId)
        val activityLog = ActivityLog(
            user = user,
            file = savedFile,
            actionType = ActionType.UPLOAD
        )

        activityLogService.saveLog(activityLog)

        return ResponseEntity.ok(savedFile)
    }

    @GetMapping("/{id}")
    fun downloadFile(@PathVariable id: Long): ResponseEntity<Resource> {

        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val user =  userService.getUserById(userId)

        val resource = fileStorage.getFile(id, userId)
        val file = fileService.getFileById(id)
        val filename = resource.filename ?: "downloaded_file"

        val activityLog = ActivityLog(
            user = user,
            file = file,
            actionType = ActionType.DOWNLOAD
        )

        activityLogService.saveLog(activityLog)

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

    @PostMapping("/share/{id}")
    fun shareFile(
        @PathVariable id : Long,
        @RequestBody request : ShareFileRequest
    ) : ResponseEntity<String> {

        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        fileAccessService.shareFile(
            ownerId = userId,
            fileId = id,
            target = request.targetId,
            accessType = request.accessType
        )

        val user = userService.getUserById(userId)
        val file = fileService.getFileById(id)
        val activityLog = ActivityLog(
            user = user,
            file = file,
            actionType = ActionType.SHARE
        )

        activityLogService.saveLog(activityLog)

        return ResponseEntity.ok("File shared successfully.")
    }

    @DeleteMapping("/{file_id}/access/{target_id}")
    fun removeAccess(
        @PathVariable("target_id") targetId : Long,
        @PathVariable("file_id") fileId : Long
    ) : ResponseEntity<String> {

        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        return try {
            fileAccessService.removeAccess(fileId, userId, targetId)
            ResponseEntity.ok("File access removed successfully.")

        } catch (e : IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }
    }


    @DeleteMapping("/delete/file/{id}")
    fun deleteFile(@PathVariable id : Long) : ResponseEntity<Void> {

        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val user =  userService.getUserById(userId)

        val file = fileService.getFileById(id)

        val activityLog = ActivityLog(
            user = user,
            file = file,
            actionType = ActionType.DELETE
        )
        activityLogService.saveLog(activityLog)

        fileStorage.deleteFile(id, userId)
        return ResponseEntity.noContent().build()
    }

}