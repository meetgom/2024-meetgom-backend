package com.meetgom.backend.service

import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.model.domain.user.User
import com.meetgom.backend.repository.UserRepository
import com.meetgom.backend.type.UserType
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
) {
    fun signUpStandard(displayName: String, email: String, password: String): User {
        if (userRepository.existsByEmail(email)) {
            throw AuthExceptions.EMAIL_ALREADY_EXISTS.toException()
        }
        val userEntity = User(
            displayName = displayName,
            userType = UserType.STANDARD,
            email = email,
            password = password,
        )
            .toEntity()
        val signedUpUserEntity = userRepository.save(userEntity)
            .toDomain()
        return signedUpUserEntity
    }
}