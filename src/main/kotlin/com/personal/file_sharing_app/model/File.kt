package com.personal.file_sharing_app.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "files")
data class File(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    val parentFolderId : Long,

    val fileName : String,

    val size : Long,

    val contentType : String,

    val path : String,

    val uploadedAt : LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    val owner : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    val folder : Folder? = null,

    @OneToMany(
        mappedBy = "file",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val accesses : List<FileAccess> = listOf(),

    @OneToMany(
        mappedBy = "file",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val activityLogs : List<ActivityLog> = listOf(),

    @ManyToMany
    @JoinTable(
        name = "file_tags",
        joinColumns = [JoinColumn(name = "file_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags : MutableList<Tag> = mutableListOf(),

    @OneToMany(
        mappedBy = "file",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val comments : List<Comment> = listOf()

)













