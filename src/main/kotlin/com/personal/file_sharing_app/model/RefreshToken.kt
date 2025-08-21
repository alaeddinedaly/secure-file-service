package com.personal.file_sharing_app.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant


@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    val userId : Long,

    val expiresAt : Instant,

    val hashedToken : String,

    val createdAt : Instant = Instant.now()
)
