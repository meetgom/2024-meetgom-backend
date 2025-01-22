package com.meetgom.backend.entity.user

import jakarta.persistence.*

@Entity(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    val name: String? = null,

    @Column(name = "is_anonymous_user")
    val isAnonymousUser: Boolean = false,

    @OneToOne(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val standardUserEntity: StandardUserEntity? = null,

    @OneToOne(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val anonymousUser: AnonymousUserEntity? = null
)