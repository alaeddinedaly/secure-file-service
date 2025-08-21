package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService : AuthService
) {

    data class RegisterRequest(
        val username : String,
        val password : String,
        val email : String
    )

    data class LoginRequest(
        val username : String,
        val password : String,
    )

    data class RefreshRequest(
        val refreshToken : String
    )

    @PostMapping("/register")
    fun register(
        @RequestBody body : RegisterRequest
    ) : User {

         return authService.registerUser(
             username = body.username,
             password = body.password,
             email = body.email
         )

    }

    @PostMapping("/login")
    fun login(
        @RequestBody body : LoginRequest
    ) : AuthService.TokenPair {

        return authService.loginUser(
            username = body.username,
            password = body.password,
        )
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body : RefreshRequest
    ) : AuthService.TokenPair {

        return authService.refresh(
            refreshToken = body.refreshToken
        )
    }
}