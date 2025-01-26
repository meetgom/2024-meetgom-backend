package com.meetgom.backend.entity.participant

import com.meetgom.backend.model.domain.participant.ParticipantRole
import com.meetgom.backend.type.ParticipantRoleType
import jakarta.persistence.*

@Entity(name = "participant_role")
data class ParticipantRoleEntity(
    @Id
    @Column(name = "participant_role")
    @Enumerated(EnumType.STRING)
    val participantRole: ParticipantRoleType,

    @Column(name = "description", length = 256)
    val description: String? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "participantRole",
        fetch = FetchType.LAZY
    )
    var participantRolePermissions: MutableList<ParticipantRolePermissionEntity>? = null
) {
    fun toDomain(): ParticipantRole {
        return ParticipantRole(
            role = this.participantRole,
            description = this.description,
            rolePermissions = this.participantRolePermissions?.map { it.participantRolePermissionPrimaryKey.participantRolePermission }
                ?.toSet() ?: setOf()
        )
    }
}