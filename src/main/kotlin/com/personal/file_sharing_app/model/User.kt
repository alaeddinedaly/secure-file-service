package com.personal.file_sharing_app.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    @Column(unique = true, nullable = false)
    var username : String,

    @Column(unique = true, nullable = false)
    var email : String,

    var passwordHash : String,

    @Enumerated(EnumType.STRING)
    val role : Role = Role.USER,

    val createdAt : LocalDateTime = LocalDateTime.now(),

    @OneToMany(
        mappedBy = "owner",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val files : List<File> = listOf(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val activityLogs : List<ActivityLog> = listOf()

)

enum class Role {
    USER,ADMIN
}
