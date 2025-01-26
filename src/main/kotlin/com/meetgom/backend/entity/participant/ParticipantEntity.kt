package com.meetgom.backend.entity.participant

import com.meetgom.backend.entity.common.TimeZoneEntity
import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.entity.user.UserEntity
import com.meetgom.backend.model.domain.participant.Participant
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
    @JoinColumn(name = "participant_role")
    val role: ParticipantRoleEntity,

    @ManyToOne
    @JoinColumn(name = "participant_time_zone_id")
    val timeZoneEntity: TimeZoneEntity,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "participant_id")
    var availableTimeSlotEntities: MutableList<ParticipantAvailableTimeSlotEntity>,
) {
    fun toDomain(): Participant {
        return Participant(
            eventSheetCode = this.eventSheet.eventCode.eventCode,
            user = user.toDomain(),
            role = role.participantRole,
            timeZone = timeZoneEntity.toDomain(),
            availableTimeSlots = availableTimeSlotEntities.map { it.toDomain() }
        )
    }
}