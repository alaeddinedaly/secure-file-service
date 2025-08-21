package com.personal.file_sharing_app.storage

import com.personal.file_sharing_app.model.File
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface FileStorage {

    fun uploadFile(userId : Long,file : MultipartFile, folderId : Long? = null) : File
    fun getFile(fileId : Long, userId : Long) : Resource
    fun listOfFiles(userId : Long) : List<File>
    fun deleteFile(fileId : Long, userId : Long)
}