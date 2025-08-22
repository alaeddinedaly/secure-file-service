package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.dto.UpdateUserRequest
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.UserRepository
import com.personal.file_sharing_app.security.HashEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository : UserRepository,
    private val hashEncoder : HashEncoder
) {

    fun getUserById(userId : Long) : User {
        return userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found.") }
    }

    fun getUserByUsername(username : String) : User {
        return userRepository.findByUsername(username)
    }

    fun updateUserProfile(userId : Long, updateUserRequest : UpdateUserRequest) : User {

        val user = getUserById(userId)
        user.username = updateUserRequest.username ?: user.username
        user.email = updateUserRequest.email ?: user.email

        return userRepository.save(user)
    }

    fun changePassword(userId : Long, oldPassword : String, newPassword : String) {

        val user = getUserById(userId)
        if(!hashEncoder.matches(oldPassword,newPassword)) {
            throw RuntimeException("Invalid old password.")
        }

        user.passwordHash = hashEncoder.encode(newPassword)
        userRepository.save(user)
    }

    fun deleteUser(userId : Long) {
        userRepository.deleteById(userId)
    }
}