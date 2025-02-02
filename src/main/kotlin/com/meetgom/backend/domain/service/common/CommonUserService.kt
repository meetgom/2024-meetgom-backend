package com.meetgom.backend.domain.service.common

import com.meetgom.backend.data.entity.user.UserEntity
import com.meetgom.backend.data.repository.UserRepository
import com.meetgom.backend.domain.model.user.User
import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.security.SecurityConfig
import com.meetgom.backend.type.UserType
import org.springframework.stereotype.Service

interface CommonUserService {
    fun createUser(
        userType: UserType,
        userName: String,
        email: String? = null,
        password: String
    ): UserEntity
}

@Service("commonUserServiceImpl")
class CommonUserServiceImpl(
    securityConfig: SecurityConfig,
    private val userRepository: UserRepository,
) : CommonUserService {
    private var encoder = securityConfig.passwordEncoder()

    override fun createUser(
        userType: UserType,
        userName: String,
        email: String?,
        password: String
    ): UserEntity {
        val checkedEmail = if (userType == UserType.STANDARD) email
            ?: throw AuthExceptions.REQUIRED_EMAIL_ARGUMENT.toException() else null

        val encodedPassword = encoder.encode(password)

        val userEntity = User(
            userName = userName,
            userType = userType,
            email = checkedEmail,
            password = encodedPassword,
        )
            .toEntity()

        val createdUser = userRepository.save(userEntity)
        return createdUser
    }
}