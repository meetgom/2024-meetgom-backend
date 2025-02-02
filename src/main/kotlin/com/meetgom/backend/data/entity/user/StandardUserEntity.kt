package com.meetgom.backend.data.entity.user

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity(name = "standard_user")
class StandardUserEntity(
    @Id
    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id"
    )
    var user: UserEntity,

    @Column(name = "email", length = 256, unique = true)
    val email: String,

    @Column(name = "password", length = 256)
    val password: String,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: ZonedDateTime? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: ZonedDateTime? = null,
)