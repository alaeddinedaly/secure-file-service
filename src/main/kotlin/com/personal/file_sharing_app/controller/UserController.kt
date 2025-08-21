package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository
) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {

        val currentUserId = SecurityContextHolder.getContext().authentication.principal
        if (currentUserId != id) {
            return ResponseEntity.status(403).build() // Forbidden
        }

        val user = userRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(user)
    }

    // Get currently authenticated user
    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<User> {
        val currentUser = SecurityContextHolder.getContext().authentication.principal as User
        return ResponseEntity.ok(currentUser)
    }

}