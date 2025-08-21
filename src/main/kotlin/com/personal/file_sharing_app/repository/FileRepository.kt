package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.Folder
import com.personal.file_sharing_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long> {

    fun findByOwnerId(ownerId : Long) : List<File>

    fun findByFolderId(folderId : Long) : List<File>

    fun findByFileNameContainingIgnoreCase(fileName : String) : List<File>

    fun findByFileName(fileName : String) : List<File>

    fun findAllByOwnerId(ownerId: Long) : List<File>

}