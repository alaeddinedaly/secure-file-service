package com.personal.file_sharing_app.dto


data class RegisterRequest(
    val username : String,
    val password : String,
    val email : String
)