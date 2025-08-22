package com.personal.file_sharing_app.model

import jakarta.persistence.Column
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
import org.hibernate.boot.spi.AccessType
import java.time.LocalDateTime


@Entity
@Table(name = "file_accesses")
data class FileAccess(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    val file : File,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    val target : User,

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type")
    var accessType : FileAccessType,

    val sharedAt : LocalDateTime = LocalDateTime.now()
)

enum class FileAccessType {
    READ, WRITE, OWNER
}
