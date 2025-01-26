package com.meetgom.backend.entity.participant

import com.meetgom.backend.model.domain.participant.ParticipantAvailableTimeSlot
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Entity(name = "participant_available_time_slot")
class ParticipantAvailableTimeSlotEntity(
    @EmbeddedId
    val participantAvailableTimeSlotPrimaryKey: ParticipantAvailableTimeSlotPrimaryKey,

    @Column(name = "end_date_time")
    var endTime: LocalTime,

    @MapsId("participantId")
    @ManyToOne(targetEntity = ParticipantEntity::class)
    @JoinColumn(name = "participant_id")
    var participantEntity: ParticipantEntity? = null,
) {
    fun toDomain(): ParticipantAvailableTimeSlot {
        return ParticipantAvailableTimeSlot(
            participantId = this.participantAvailableTimeSlotPrimaryKey.participantId,
            date = this.participantAvailableTimeSlotPrimaryKey.date,
            startTime = this.participantAvailableTimeSlotPrimaryKey.startTime,
            endTime = this.endTime
        )
    }
}

@Embeddable
data class ParticipantAvailableTimeSlotPrimaryKey(
    @Column(name = "participant_id")
    val participantId: Long,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "start_date_time")
    val startTime: LocalTime
) : Serializable