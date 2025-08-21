package com.personal.file_sharing_app.repository

import com.personal.file_sharing_app.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository


interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {

    fun findByUserIdAndHashedToken(userId : Long, hashedToken : String) : RefreshToken?

    fun deleteByUserIdAndHashedToken(userId : Long, hashedToken : String)

    fun deleteAllByUserId(userId : Long)

}