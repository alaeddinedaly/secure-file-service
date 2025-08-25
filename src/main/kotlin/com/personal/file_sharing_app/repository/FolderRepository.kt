package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.Folder
import com.personal.file_sharing_app.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface FolderRepository : JpaRepository<Folder, Long> {

    fun findByOwner(owner : User) : List<Folder>

    fun findByName(folderName : String) : Folder?

    fun findByParentFolder(parentFolder : Folder) : List<Folder>

    fun findByNameContainingIgnoreCase(name : String) : Folder?
}