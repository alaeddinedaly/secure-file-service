package com.personal.file_sharing_app.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "activity_logs")
data class ActivityLog(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    val file : File? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    val folder : Folder? = null,

    @Enumerated(EnumType.STRING)
    val actionType : ActionType,

    val timestamp : LocalDateTime = LocalDateTime.now()
)

enum class ActionType{
    UPLOAD, DOWNLOAD, DELETE, SHARE
}
