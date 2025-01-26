package com.meetgom.backend.service

import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.model.domain.user.User
import com.meetgom.backend.repository.UserRepository
import com.meetgom.backend.security.SecurityConfig
import com.meetgom.backend.type.UserType
import org.springframework.stereotype.Service

@Service
class UserService(
    private val securityConfig: SecurityConfig,
    private val userRepository: UserRepository,
) {
    private var encoder = securityConfig.passwordEncoder()

    fun createStandardUser(displayName: String, email: String, password: String): User {
        if (userRepository.existsByEmail(email)) {
            throw AuthExceptions.EMAIL_ALREADY_EXISTS.toException()
        }
        val encodedPassword = encoder.encode(password)
        val userEntity = User(
            displayName = displayName,
            userType = UserType.STANDARD,
            email = email,
            password = encodedPassword,
        )
            .toEntity()
        val createdUser = userRepository.save(userEntity)
            .toDomain()
        return createdUser
    }
}