package com.meetgom.backend.entity.user

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity(name = "anonymous_user")
class AnonymousUserEntity(
    @Id
    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        nullable = false
    )
    var user: UserEntity,

    @Column(name = "password")
    val password: String,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: ZonedDateTime? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: ZonedDateTime? = null,
)