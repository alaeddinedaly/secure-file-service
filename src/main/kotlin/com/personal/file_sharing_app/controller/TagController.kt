package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.dto.TagRequest
import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.Tag
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.FileAccessRepository
import com.personal.file_sharing_app.repository.UserRepository
import com.personal.file_sharing_app.service.FileAccessService
import com.personal.file_sharing_app.service.FileService
import com.personal.file_sharing_app.service.TagService
import com.personal.file_sharing_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/tags")
class TagController(
    private val tagService : TagService,
    private val fileAccessService: FileAccessService,
    private val userService : UserService,
    private val fileService: FileService
){

    @PostMapping("/add")
    fun addTagToFile(
        @RequestBody request : TagRequest
    ) : ResponseEntity<File> {

        val userId = userService.getCurrentUserId()

        val user = userService.getUserById(userId)

        val file = fileService.getFileById(request.fileId)

        if(!fileAccessService.checkAccess(user, file)) {
            throw IllegalArgumentException("User doesn't have access.")
        }

        tagService.addTagToFile(file, request.tagName)

        return ResponseEntity.ok(file)

    }

    @DeleteMapping
    fun removeTagFromFile(
        @RequestBody request : TagRequest
    ) : ResponseEntity<File> {

        val userId = userService.getCurrentUserId()

        val user = userService.getUserById(userId)

        val file = fileService.getFileById(request.fileId)

        if(!fileAccessService.checkAccess(user, file)) {
            throw IllegalArgumentException("User doesn't have access.")
        }

        tagService.removeTagFromFile(file, request.tagName)

        return ResponseEntity.ok(file)

    }
}