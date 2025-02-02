package com.meetgom.backend.domain.service

import com.meetgom.backend.domain.model.user.User
import com.meetgom.backend.domain.service.common.CommonUserService
import com.meetgom.backend.type.UserType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class UserService(
    private val commonUserService: CommonUserService
) {
    fun createStandardUser(userName: String, email: String, password: String): User {
        return commonUserService.createUser(
            userType = UserType.STANDARD,
            userName = userName,
            email = email,
            password = password
        ).toDomain()
    }

    fun createAnonymousUser(userName: String, password: String): User {
        return commonUserService.createUser(
            userType = UserType.ANONYMOUS,
            userName = userName,
            password = password
        ).toDomain()
    }
}