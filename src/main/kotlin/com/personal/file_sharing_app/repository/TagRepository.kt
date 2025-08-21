package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {

    fun findByName(name : String) : Tag?

    fun existsByName(name : String) : Boolean
}