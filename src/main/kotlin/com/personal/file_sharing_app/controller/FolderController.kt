package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.dto.FolderRequest
import com.personal.file_sharing_app.model.Folder
import com.personal.file_sharing_app.service.FolderService
import com.personal.file_sharing_app.service.LocalStorageService
import com.personal.file_sharing_app.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController



@RestController
@RequestMapping("/folders")
class FolderController(
    private val folderService : FolderService,
    private val userService: UserService
) {

    @GetMapping
    fun getFolder(@RequestBody request : FolderRequest) : ResponseEntity<Folder> {

        val folder = folderService.getFolderByName(request.folderName)

        val userId = userService.getCurrentUserId()

        if(folder?.owner?.id != userId) {
            throw IllegalArgumentException("User doesn't have access.")
        }

        return ResponseEntity.ok(folder)

    }

    @GetMapping("/{folderName}/download")
    fun downloadFolder(@PathVariable folderName : String) : ResponseEntity<ByteArray> {

        val folder = folderService.getFolderByName(folderName)

        val userId = userService.getCurrentUserId()

        if(folder?.owner?.id != userId) {
            throw IllegalArgumentException("User doesn't have access.")
        }

        val zipBytes = folderService.downloadFolderAsZip(folderName)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$folderName.zip\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipBytes)
    }


    @DeleteMapping
    fun deleteFolder(@RequestBody request : FolderRequest) : ResponseEntity<String> {

        val folder = folderService.getFolderByName(request.folderName)

        val userId = userService.getCurrentUserId()

        if(folder?.owner?.id != userId) {
            throw IllegalArgumentException("User doesn't have access.")
        }

        folderService.deleteFolder(request.folderName)

        return ResponseEntity.ok("Folder deleted successfully.")

    }

    @PostMapping
    fun createFolder(@RequestBody request : FolderRequest) : ResponseEntity<Folder> {

        val userId = userService.getCurrentUserId()

        val user = userService.getUserById(userId)

        val folder = folderService.createFolder(request.folderName, user)

        return ResponseEntity.ok(folder)

    }


}















