package com.meetgom.backend.data.entity.participant

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.meetgom.backend.data.entity.common.TimeZoneEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.user.UserEntity
import com.meetgom.backend.domain.model.participant.Participant
import jakarta.persistence.*

@Entity(name = "participant")
class ParticipantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "event_sheet_id")
    @JsonBackReference
    val eventSheetEntity: EventSheetEntity,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne
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
    @JsonManagedReference
    var availableTimeSlotEntities: MutableList<ParticipantAvailableTimeSlotEntity>,
) {
    fun toDomain(): Participant {
        return Participant(
            eventSheetCode = this.eventSheetEntity.eventCodeEntity.eventCode,
            user = user.toDomain(),
            role = role.participantRole,
            timeZone = timeZoneEntity.toDomain(),
            availableTimeSlots = availableTimeSlotEntities.map { it.toDomain() }
        )
    }
}