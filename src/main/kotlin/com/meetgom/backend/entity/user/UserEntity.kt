package com.meetgom.backend.entity.user

import jakarta.persistence.*

@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String? = null,
    val isAnonymousUser: Boolean = false,
    @OneToOne
    val standardUserEntity: StandardUserEntity? = null,
    @OneToOne
    val anonymousUserEntity: AnonymousUserEntity? = null
)