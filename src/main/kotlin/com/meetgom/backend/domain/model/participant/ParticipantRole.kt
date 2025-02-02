package com.meetgom.backend.domain.model.participant

import com.meetgom.backend.type.ParticipantRolePermissionType
import com.meetgom.backend.type.ParticipantRoleType

data class ParticipantRole(
    val role: ParticipantRoleType,
    val description: String?,
    val rolePermissions: Set<ParticipantRolePermissionType>
)