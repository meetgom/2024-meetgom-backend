package com.meetgom.backend.model.domain.participant

import com.meetgom.backend.type.ParticipantRolePermissionType
import com.meetgom.backend.type.ParticipantRoleType

data class ParticipantRole(
    val role: ParticipantRoleType,
    val description: String?,
    val rolePermissions: Set<ParticipantRolePermissionType>
)