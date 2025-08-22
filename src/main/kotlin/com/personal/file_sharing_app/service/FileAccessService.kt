package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.FileAccess
import com.personal.file_sharing_app.model.FileAccessType
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.FileAccessRepository
import org.springframework.stereotype.Service


@Service
class FileAccessService(
    private val fileAccessRepository : FileAccessRepository,
    private val fileService: FileService,
    private val userService: UserService
) {

    fun getFileAccess(user : User, file : File) : FileAccess? =
        fileAccessRepository.findByUserAndFile(user, file)



    fun shareFile(fileId : Long, ownerId : Long, target : Long, accessType : FileAccessType) : FileAccess {

        val owner = userService.getUserById(ownerId)
        val targetUser = userService.getUserById(target)

        val file = fileService.getFileById(fileId)


        val fileAccess = getFileAccess(user = owner, file = file)

        val ownerAccess = fileAccess?.accessType

        if(ownerAccess == null || ownerAccess != FileAccessType.WRITE) {
            throw IllegalArgumentException("You don't have permission to share file.")
        }

        val targetAccess = getFileAccess(targetUser, file)

        val sharedFile = if(targetAccess == null) {
            FileAccess(
                file = file,
                user = owner,
                target = targetUser,
                accessType = accessType,
            )
        } else {
            targetAccess.accessType = accessType

            targetAccess
        }

        return fileAccessRepository.save(sharedFile)
    }
}