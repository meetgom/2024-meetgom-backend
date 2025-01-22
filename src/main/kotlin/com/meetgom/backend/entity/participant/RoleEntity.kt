package com.meetgom.backend.entity.participant

import com.meetgom.backend.type.RoleType
import jakarta.persistence.*

@Entity(name = "role")
data class RoleEntity(
    @Id
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: RoleType,

    @Column(name="description", length = 256)
    val description: String? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "role",
        fetch = FetchType.LAZY
    )
    var rolePermissions: MutableList<RolePermissionEntity>? = null
)