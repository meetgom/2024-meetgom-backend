package com.meetgom.backend.data.entity.user

import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.domain.model.user.User
import com.meetgom.backend.type.UserType
import com.meetgom.backend.type.UserType.*
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_name")
    val userName: String,

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    val userType: UserType,

    @Column(name = "deleted_at")
    val deletedAt: ZonedDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: ZonedDateTime? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: ZonedDateTime? = null,

    @OneToOne(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "user_id",
        nullable = true
    )
    var standardUserEntity: StandardUserEntity? = null,

    @OneToOne(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
    )
    @JoinColumn(
        name = "user_id",
        nullable = true
    )
    var anonymousUser: AnonymousUserEntity? = null
) {
    fun toDomain(): User {
        val (email, password) = when (userType) {
            STANDARD -> {
                val standardUser = standardUserEntity ?: throw AuthExceptions.NOT_FOUND_USER.toException()
                Pair(standardUser.email, standardUser.password)
            }

            ANONYMOUS -> {
                val anonymousUser = anonymousUser ?: throw AuthExceptions.NOT_FOUND_USER.toException()
                Pair(null, anonymousUser.password)
            }
        }
        return User(
            id = id,
            userName = userName,
            userType = userType,
            email = email,
            password = password,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}