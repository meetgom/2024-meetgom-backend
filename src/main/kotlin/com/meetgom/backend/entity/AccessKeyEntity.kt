package com.meetgom.backend.entity

import com.meetgom.backend.type.Role
import jakarta.persistence.*
import java.io.Serializable


@Entity(name = "event_access_key")
class AccessKeyEntity(
    @EmbeddedId
    val accessKeyPrimaryKey: AccessKeyPrimaryKey,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role
)


@Embeddable
data class AccessKeyPrimaryKey(
    @ManyToOne(targetEntity = EventEntity::class)
    @JoinColumn(name = "event_id")
    val event: EventEntity,

    @Column(name = "access_key", length = 256)
    val accessKey: String
) : Serializable