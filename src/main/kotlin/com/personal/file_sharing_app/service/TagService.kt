package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.File
import com.personal.file_sharing_app.model.Tag
import com.personal.file_sharing_app.repository.FileRepository
import com.personal.file_sharing_app.repository.TagRepository
import org.springframework.stereotype.Service


@Service
class TagService(
    private val tagRepository: TagRepository,
    private val fileRepository: FileRepository
) {

    fun getTag(tagName : String) : Tag? =
        tagRepository.findByName(tagName)

    fun addTagToFile(file : File, tagName : String) : Tag {

        val tag = getTag(tagName) ?: tagRepository.save(Tag(name = tagName))

        if(!file.tags.contains(tag)) {
            file.tags.add(tag)
        }

        fileRepository.save(file)

        return tag
    }

    fun removeTagFromFile(file : File, tagName: String) : File {

        val tag = getTag(tagName)

        if(file.tags.contains(tag)) {
            file.tags.remove(tag)
            fileRepository.save(file)
        }

        return file
    }

    fun getFilesByTag(tagName : String) : List<File> {

        val tag = getTag(tagName) ?: throw IllegalArgumentException("Tag not found.")

        return tag.files
    }
 }