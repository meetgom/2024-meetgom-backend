package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "event_sheet_time_slot")
class EventSheetTimeSlotEntity(
    @EmbeddedId
    val eventSheetTimeSlotPrimaryKey: EventSheetTimeSlotPrimaryKey,

    @Column(name = "end_date_time")
    var endDateTime: LocalDateTime,

    @MapsId("eventSheetId")
    @ManyToOne(targetEntity = EventSheetEntity::class)
    @JoinColumn(name = "event_sheet_id")
    var eventSheetEntity: EventSheetEntity? = null,
) {
    fun toDomain(): EventSheetTimeSlot {
        return EventSheetTimeSlot(
            eventSheetId = this.eventSheetTimeSlotPrimaryKey.eventSheetId,
            startDateTime = this.eventSheetTimeSlotPrimaryKey.startDateTime,
            endDateTime = this.endDateTime
        )
    }
}

@Embeddable
data class EventSheetTimeSlotPrimaryKey(
    val eventSheetId: Long? = null,

    @Column(name = "start_date_time")
    val startDateTime: LocalDateTime
) : Serializable
