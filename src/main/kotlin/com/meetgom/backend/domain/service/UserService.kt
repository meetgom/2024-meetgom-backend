package com.meetgom.backend.domain.service

import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.domain.model.user.User
import com.meetgom.backend.data.repository.UserRepository
import com.meetgom.backend.security.SecurityConfig
import com.meetgom.backend.type.UserType
import org.springframework.stereotype.Service

@Service
class UserService(
    securityConfig: SecurityConfig,
    private val userRepository: UserRepository,
) {
    private var encoder = securityConfig.passwordEncoder()

    fun createStandardUser(userName: String, email: String, password: String): User {
        if (userRepository.existsByEmail(email)) {
            throw AuthExceptions.EMAIL_ALREADY_EXISTS.toException()
        }
        val encodedPassword = encoder.encode(password)
        val userEntity = User(
            userName = userName,
            userType = UserType.STANDARD,
            email = email,
            password = encodedPassword,
        )
            .toEntity()
        val createdUser = userRepository.save(userEntity)
            .toDomain()
        return createdUser
    }

    fun createAnonymousUser(userName: String, password: String): User {
        val encodedPassword = encoder.encode(password)
        val userEntity = User(
            userName = userName,
            userType = UserType.ANONYMOUS,
            email = null,
            password = encodedPassword,
        )
            .toEntity()
        val createdUser = userRepository.save(userEntity)
            .toDomain()
        return createdUser
    }
}