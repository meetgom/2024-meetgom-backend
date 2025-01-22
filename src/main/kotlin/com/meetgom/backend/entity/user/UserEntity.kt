package com.meetgom.backend.entity.user

import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.model.domain.user.User
import com.meetgom.backend.type.UserType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "display_name")
    val displayName: String,

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    val userType: UserType,

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
            UserType.STANDARD -> {
                val standardUser = standardUserEntity ?: throw AuthExceptions.NOT_FOUND_USER.toException()
                Pair(standardUser.email, standardUser.password)
            }

            UserType.ANONYMOUS -> Pair(null, null)
        }
        return User(
            id = id,
            displayName = displayName,
            userType = userType,
            email = email,
            password = password,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}