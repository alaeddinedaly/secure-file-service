package com.personal.file_sharing_app.controller

import com.personal.file_sharing_app.dto.LoginRequest
import com.personal.file_sharing_app.dto.RefreshRequest
import com.personal.file_sharing_app.dto.RegisterRequest
import com.personal.file_sharing_app.model.ActionType
import com.personal.file_sharing_app.model.ActivityLog
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.service.ActivityLogService
import com.personal.file_sharing_app.service.AuthService
import com.personal.file_sharing_app.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService : AuthService,
    private val activityLogService: ActivityLogService,
    private val userService : UserService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody body : RegisterRequest
    ) : User {

        val user = authService.registerUser(
            username = body.username,
            password = body.password,
            email = body.email
        )

        val activityLog = ActivityLog(
            user = user,
            actionType = ActionType.REGISTER
        )
        activityLogService.saveLog(activityLog)

        return user
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body : LoginRequest
    ) : AuthService.TokenPair {

        val user = userService.getUserByUsername(body.username)
        val activityLog = ActivityLog(
            user = user,
            actionType = ActionType.LOGIN
        )

        activityLogService.saveLog(activityLog)

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