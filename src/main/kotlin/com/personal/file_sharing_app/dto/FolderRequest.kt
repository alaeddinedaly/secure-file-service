package com.personal.file_sharing_app.dto

import com.personal.file_sharing_app.model.User

data class FolderRequest(
    val user : User?,
    val folderName : String
)
