package com.meetgom.backend.entity.participant

import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.entity.user.UserEntity
import jakarta.persistence.*

@Entity(name = "participant")
class ParticipantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "event_sheet_id")
    val eventSheet: EventSheetEntity,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,


    @OneToOne
    @JoinColumn(name = "role")
    val role: RoleEntity,
)