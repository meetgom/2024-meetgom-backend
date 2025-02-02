package com.meetgom.backend.data.entity.participant

import com.meetgom.backend.type.ParticipantRolePermissionType
import com.meetgom.backend.type.ParticipantRoleType
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "participant_role_permission")
data class ParticipantRolePermissionEntity(
    @EmbeddedId
    val participantRolePermissionPrimaryKey: ParticipantRolePermissionPrimaryKey,

    @MapsId("participant_role")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_role", referencedColumnName = "participant_role", nullable = false)
    var participantRole: ParticipantRoleEntity? = null,
)

@Embeddable
data class ParticipantRolePermissionPrimaryKey(
    @Enumerated(EnumType.STRING)
    @Column(name = "participant_role", nullable = false)
    val participantRole: ParticipantRoleType,

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_role_permission", nullable = false)
    val participantRolePermission: ParticipantRolePermissionType
) : Serializable