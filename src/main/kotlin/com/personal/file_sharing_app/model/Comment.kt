package com.personal.file_sharing_app.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.cglib.core.Local
import java.time.LocalDateTime


@Entity
@Table(name = "comments")
data class Comment(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    val file : File,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val author : User,


    val message : String,

    val createdAt : LocalDateTime = LocalDateTime.now()
)
