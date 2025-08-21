package com.personal.file_sharing_app.dto

import com.personal.file_sharing_app.model.Role

data class UpdateUserRequest(
    val username : String? = null,
    val password : String? = null,
    val email : String? = null,
    val role : Role? = null,
)
