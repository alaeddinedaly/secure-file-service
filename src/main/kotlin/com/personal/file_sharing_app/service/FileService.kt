package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.FileAccess
import com.personal.file_sharing_app.model.FileAccessType
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.FileAccessRepository
import com.personal.file_sharing_app.repository.FileRepository
import com.personal.file_sharing_app.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class FileService(
    private val fileRepository : FileRepository,
    private val userRepository: UserRepository,
    private val fileAccessRepository: FileAccessRepository
){

    fun getFileById(fileId : Long) : File {
        return fileRepository.findById(fileId)
            .orElseThrow { RuntimeException("File not found.") }
    }

//    fun shareFile(userId : Long, fileId : Long, accessType : FileAccessType) : FileAccess {
//
//        val file = fileRepository.findById(fileId)
//            .orElseThrow { RuntimeException("File not found.") }
//
//        val user = userRepository.findById(userId)
//            .orElseThrow { RuntimeException("User not found.") }
//
//        val fileAccess = FileAccess(
//            file = file,
//            user = user,
//            accessType = accessType
//        )
//
//        return fileAccessRepository.save(fileAccess)
//    }
}