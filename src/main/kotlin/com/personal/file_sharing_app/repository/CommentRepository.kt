package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.Comment
import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {

    fun findByFile(file : File) : List<Comment>

    fun findByAuthor(author : User) : List<Comment>

}