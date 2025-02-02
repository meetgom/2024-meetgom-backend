package com.meetgom.backend.data.entity.participant

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetgom.backend.domain.model.participant.ParticipantAvailableTimeSlot
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
    @JsonIgnore
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
    val participantId: Long? = null,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "start_date_time")
    val startTime: LocalTime
) : Serializable