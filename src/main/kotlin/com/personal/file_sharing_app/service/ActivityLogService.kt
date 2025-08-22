package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.ActionType
import com.personal.file_sharing_app.model.ActivityLog
import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.repository.ActivityLogRepository
import com.personal.file_sharing_app.repository.FileRepository
import com.personal.file_sharing_app.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class ActivityLogService(
    private val activityLogRepository : ActivityLogRepository,
    private val fileRepository: FileRepository,
    private val userRepository: UserRepository
) {

    fun saveLog(activityLog : ActivityLog) : ActivityLog {
        return activityLogRepository.save(activityLog)
    }

    fun getLogsByFileId(fileId : Long) : List<ActivityLog> {

        val file = fileRepository.findById(fileId)
            .orElseThrow { RuntimeException("File not found.") }

        return activityLogRepository.findByFile(file = file)
    }

    fun getLogsByUserId(userId : Long) : List<ActivityLog> {

        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("File not found.") }

        return activityLogRepository.findByUser(user = user)
    }

    fun getLogsByUserIdAndFileId(userId : Long, fileId : Long) : List<ActivityLog> {

        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("File not found.") }

        val file = fileRepository.findById(fileId)
            .orElseThrow { RuntimeException("File not found.") }

        return activityLogRepository.findByFileAndUser(file, user)
    }

    fun findLogsByActionType(actionType : ActionType) : List<ActivityLog> =
        activityLogRepository.findByActionType(actionType)

}