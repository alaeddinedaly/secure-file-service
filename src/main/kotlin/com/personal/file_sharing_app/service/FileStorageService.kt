package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.repository.FileRepository
import com.personal.file_sharing_app.repository.FolderRepository
import com.personal.file_sharing_app.repository.UserRepository
import com.personal.file_sharing_app.storage.FileStorage
import com.personal.file_sharing_app.storage.StorageService
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class FileStorageService(
    private val fileRepository : FileRepository,
    private val storageService : LocalStorageService,
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository
) : FileStorage {

    override fun uploadFile(userId: Long, file: MultipartFile, folderId : Long?) : File {

        val path = storageService.save(file)

        val owner = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not Found.") }

        val folder = folderId?.let {
            folderRepository.findById(it).orElseThrow { RuntimeException("Folder not found.") }
        }

        val savedFile = File(
            userId = userId,
            parentFolderId = folder?.id ?: 0L,
            fileName = file.originalFilename ?: "Unknown",
            path = path,
            size = file.size,
            contentType = file.contentType ?: "application/octet-stream",
            owner = owner,
            folder = folder
        )

        return fileRepository.save(savedFile)
    }

    override fun getFile(fileId: Long, userId: Long): Resource {

        val file = fileRepository.findById(fileId)
            .orElseThrow { RuntimeException("File not found.") }

        if(file.userId != userId) throw RuntimeException("Access denied.")

        return storageService.load(file.path)
    }

    override fun listOfFiles(userId: Long): List<File> =
        fileRepository.findAllByOwnerId(userId)

    override fun deleteFile(fileId: Long, userId: Long) {

        val file = fileRepository.findById(fileId)
            .orElseThrow { RuntimeException("File not found.") }

        if(file.userId != userId) throw RuntimeException("AccessDenied")

        storageService.delete(file.path)
        fileRepository.delete(file)
    }


}