package com.meetgom.backend.data.entity.event_sheet

import com.fasterxml.jackson.annotation.JsonBackReference
import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Entity(name = "event_sheet_time_slot")
class EventSheetTimeSlotEntity(
    @EmbeddedId
    val eventSheetTimeSlotPrimaryKey: EventSheetTimeSlotPrimaryKey,

    @Column(name = "end_time")
    var endTime: LocalTime,

    @MapsId("eventSheetId")
    @ManyToOne(
        targetEntity = EventSheetEntity::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "event_sheet_id")
    @JsonBackReference
    var eventSheetEntity: EventSheetEntity? = null,
) {
    fun toDomain(): EventSheetTimeSlot {
        return EventSheetTimeSlot(
            eventSheetId = this.eventSheetTimeSlotPrimaryKey.eventSheetId,
            date = this.eventSheetTimeSlotPrimaryKey.date,
            startTime = this.eventSheetTimeSlotPrimaryKey.startTime,
            endTime = this.endTime
        )
    }
}

@Embeddable
data class EventSheetTimeSlotPrimaryKey(
    val eventSheetId: Long? = null,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "start_date_time")
    val startTime: LocalTime
) : Serializable

