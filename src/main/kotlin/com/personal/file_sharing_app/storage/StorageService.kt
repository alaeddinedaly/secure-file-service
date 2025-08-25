package com.personal.file_sharing_app.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface StorageService {

    fun save(file : MultipartFile, folderName : String?) : String
    fun load(path : String) : Resource
    fun delete(path : String)

}