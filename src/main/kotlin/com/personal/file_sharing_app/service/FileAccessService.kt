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

    fun getFileAccess(user : User, file : File, target : User) : FileAccess? =
        fileAccessRepository.findByUserAndFile(user, file)

    fun checkAccess(user : User, file : File) : Boolean =
        fileAccessRepository.findByUserAndFile(user, file)?.accessType != FileAccessType.WRITE && user.id != file.owner.id


    fun shareFile(fileId : Long, ownerId : Long, target : Long, accessType : FileAccessType) : FileAccess {

        val sharer = userService.getUserById(ownerId)
        val targetUser = userService.getUserById(target)

        val file = fileService.getFileById(fileId)

        val isOwner = file.owner.id == sharer.id

        val targetAccess = getFileAccess(sharer, file, targetUser)

        val hasAccess = fileAccessRepository.findByTargetAndFile(sharer, file)

        if(!isOwner && hasAccess?.accessType != FileAccessType.WRITE) {
            throw IllegalArgumentException("You don't have permission to share file.")
        }



//        if(targetAccess?.accessType == FileAccessType.READ) {
//            throw IllegalArgumentException("You don't have permission to share file.")
//        }

        val sharedFile = if(targetAccess == null) {
            FileAccess(
                file = file,
                user = sharer,
                target = targetUser,
                accessType = accessType,
            )
        } else {
            targetAccess.accessType = accessType

            targetAccess
        }

        return fileAccessRepository.save(sharedFile)
    }

    fun removeAccess(fileId : Long, ownerId : Long, target : Long) {

        val owner = userService.getUserById(ownerId)
        val targetUser = userService.getUserById(target)

        val file = fileService.getFileById(fileId)

        val fileToDelete = getFileAccess(owner, file, targetUser)
            ?: throw IllegalArgumentException("Access not found.")

        fileAccessRepository.delete(fileToDelete)
    }
}