package com.meetgom.backend.entity.participant

import com.meetgom.backend.type.RolePermissionType
import com.meetgom.backend.type.RoleType
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "role_permission")
data class RolePermissionEntity(
    @EmbeddedId
    val rolePermissionPrimaryKey: RolePermissionPrimaryKey,

    @MapsId("role")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role", referencedColumnName = "role", nullable = false)
    var role: RoleEntity? = null,
)

@Embeddable
data class RolePermissionPrimaryKey(
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: RoleType,

    @Enumerated(EnumType.STRING)
    @Column(name = "role_permission", nullable = false)
    val rolePermission: RolePermissionType
) : Serializable