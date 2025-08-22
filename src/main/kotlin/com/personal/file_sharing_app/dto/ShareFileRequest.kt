package com.personal.file_sharing_app.dto

import com.personal.file_sharing_app.model.FileAccessType

data class ShareFileRequest(
    val targetId : Long,
    val accessType : FileAccessType = FileAccessType.READ
)
