package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.FileAccess
import com.personal.file_sharing_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FileAccessRepository : JpaRepository<FileAccess, Long> {

    fun findByUser(user : User) : List<FileAccess>

    fun findByFile(file : File) : List<FileAccess>

}