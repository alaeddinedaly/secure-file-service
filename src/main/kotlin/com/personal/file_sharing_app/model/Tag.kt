package com.personal.file_sharing_app.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table


@Entity
@Table(name = "tags")
data class Tag(


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    val name : String,

    @ManyToMany(mappedBy = "tags")
    val files : List<File> = listOf()
)
