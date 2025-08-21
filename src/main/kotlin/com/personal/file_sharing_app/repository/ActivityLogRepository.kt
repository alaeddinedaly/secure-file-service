package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.ActionType
import com.personal.file_sharing_app.model.ActivityLog
import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityLogRepository : JpaRepository<ActivityLog, Long> {

    fun findByUser(user : User) : List<ActivityLog>

    fun findByFile(file : File) : List<ActivityLog>

    fun findByActionType(actionType : ActionType) : List<ActivityLog>
}