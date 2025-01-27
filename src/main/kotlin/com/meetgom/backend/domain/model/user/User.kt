package com.meetgom.backend.domain.model.user

import com.meetgom.backend.data.entity.user.AnonymousUserEntity
import com.meetgom.backend.data.entity.user.StandardUserEntity
import com.meetgom.backend.data.entity.user.UserEntity
import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.controller.http.response.UserResponse
import com.meetgom.backend.type.UserType
import java.time.ZonedDateTime

data class User(
    val id: Long? = null,
    val userName: String,
    val userType: UserType,
    val email: String?,
    val password: String?,
    val updatedAt: ZonedDateTime? = null,
    val createdAt: ZonedDateTime? = null,
) {
    fun toEntity(): UserEntity {
        return when (userType) {
            UserType.STANDARD -> {
                val email = email ?: throw AuthExceptions.NEED_EMAIL_ARGUMENT.toException()
                val password = password ?: throw AuthExceptions.NEED_PASSWORD_ARGUMENT.toException()
                val userEntity = UserEntity(
                    userName = userName,
                    userType = userType,
                )
                val standardUserEntity = StandardUserEntity(
                    email = email,
                    password = password,
                    user = userEntity,
                )
                userEntity.standardUserEntity = standardUserEntity
                userEntity
            }

            UserType.ANONYMOUS -> {
                val password = password ?: throw AuthExceptions.NEED_PASSWORD_ARGUMENT.toException()
                val userEntity = UserEntity(
                    userName = userName,
                    userType = userType,
                )
                val anonymousUserEntity = AnonymousUserEntity(
                    password = password,
                    user = userEntity,
                )
                userEntity.anonymousUser = anonymousUserEntity
                userEntity
            }
        }
    }

    fun toResponse(): UserResponse = UserResponse(
        userName = userName,
        userType = userType,
    )
}
