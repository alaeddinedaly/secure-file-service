package com.personal.file_sharing_app.service

import com.personal.file_sharing_app.model.RefreshToken
import com.personal.file_sharing_app.model.User
import com.personal.file_sharing_app.repository.RefreshTokenRepository
import com.personal.file_sharing_app.repository.UserRepository
import com.personal.file_sharing_app.security.HashEncoder
import jakarta.transaction.Transactional
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64


@Service
class AuthService(
    private val jwtService : JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository : RefreshTokenRepository
) {

    data class TokenPair(
        val accessToken : String,
        val refreshToken : String
    )

    fun registerUser(username : String, email : String, password : String) : User {
        return userRepository.save(
            User(
                email = email,
                passwordHash = hashEncoder.encode(password),
                username = username
            )
        )
    }

    @Transactional
    fun refresh(refreshToken : String) : TokenPair {

        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw IllegalArgumentException("Invalid refresh Token.")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)

        val user = userRepository.findById(userId.toLong()).orElseThrow {
            IllegalArgumentException("Invalid refresh Token.")
        }

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(userId = user.id,hashedToken = hashed)
            ?: throw IllegalArgumentException("Refresh token not recognized.")

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id,hashed)

        val newAccessToken = jwtService.generateAccessToken(userId.toString())

        storeRefreshToken(
            userId = user.id,
            rawRefreshToken = refreshToken
        )

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional
    fun loginUser(username : String, password : String) : TokenPair {

        val user = userRepository.findByUsername(username)

        if(!hashEncoder.matches(password, user.passwordHash)) {
            throw BadCredentialsException("Invalid Credentials.")
        }

        refreshTokenRepository.deleteAllByUserId(user.id)

        val newAccessToken = jwtService.generateAccessToken(user.id.toString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toString())

        storeRefreshToken(userId = user.id, rawRefreshToken = newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId : Long, rawRefreshToken : String) {

        val hashedRefreshToken = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedRefreshToken
            )
        )
    }

    private fun hashToken(token : String) : String {

        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)

    }
 }