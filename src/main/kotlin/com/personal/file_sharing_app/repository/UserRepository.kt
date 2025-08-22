package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.Role
import com.personal.file_sharing_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email : String) : User?

    fun findByUsername(username : String) : User

    fun existsByEmail(email : String) : Boolean

    fun existsByUsername(username: String) : Boolean

}