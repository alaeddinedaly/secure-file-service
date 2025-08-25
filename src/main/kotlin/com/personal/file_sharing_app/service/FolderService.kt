package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.Folder
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.FolderRepository
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


@Service
class FolderService(
    private val folderRepository : FolderRepository,
    private val storageService : LocalStorageService
) {

    fun getFolder(folderId : Long) : Folder? =
        folderRepository.findById(folderId).orElse(null)

    fun getFoldersByParent(parentFolder: Folder) : List<Folder> =
        folderRepository.findByParentFolder(parentFolder)

    fun getFolderByOwner(owner : User) : List<Folder> =
        folderRepository.findByOwner(owner)

    fun getFolderByName(name : String) : Folder? =
        folderRepository.findByNameContainingIgnoreCase(name) ?: throw IllegalArgumentException("Folder not found.")



//    fun getOrCreateFolder(folderId: Long, folderName : String, folderOwner : User) : Folder {
//
//        return folderRepository.findById(folderId).orElseGet {
//            val newFolder = Folder(
//                id = folderId,
//                name = folderName,
//                owner = folderOwner
//            )
//            folderRepository.save(newFolder)
//        }
//    }

    fun createFolder(folderName : String, folderOwner : User) : Folder {

        if(folderRepository.findByName(folderName) != null) {
            throw IllegalArgumentException("Folder already exists.")
        }

        val folder = Folder(
            name = folderName,
            owner = folderOwner
        )

        storageService.createFolder(folderName)
        
        return folderRepository.save(folder)
    }

    fun deleteFolder(folderName: String) {

        val folder = folderRepository.findByName(folderName) ?: throw IllegalArgumentException("Folder not found.")

        storageService.deleteFolder(folderName)

        folderRepository.delete(folder)

    }

    fun downloadFolderAsZip(folderName : String) : ByteArray {

        val folderPath : Path = Paths.get(storageService.getUploadDir(), folderName)

        if(!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            throw RuntimeException("Folder not found.")
        }

        val baos = ByteArrayOutputStream()

        ZipOutputStream(baos).use { zos ->
            Files.walk(folderPath).forEach { path ->
                if(Files.isRegularFile(path)) {
                    val entryName = folderPath.relativize(path).toString()
                    zos.putNextEntry(ZipEntry(entryName))
                    Files.copy(path, zos)
                    zos.closeEntry()
                }
            }

        }

        return baos.toByteArray()
    }


}