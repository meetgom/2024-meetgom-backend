package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity(name = "event_sheet_time_slot")
class EventSheetTimeSlotEntity(
    @EmbeddedId
    val eventSheetTimeSlotPrimaryKey: EventSheetTimeSlotPrimaryKey,

    @Column(name = "start_time")
    var startTime: Int,

    @Column(name = "end_time")
    var endTime: Int,

    @MapsId("eventSheetId")
    @ManyToOne(targetEntity = EventSheetEntity::class)
    @JoinColumn(name = "event_sheet_id")
    var eventSheetEntity: EventSheetEntity? = null,
) {
    fun toDomain(): EventSheetTimeSlot {
        return EventSheetTimeSlot(
            eventSheetId = this.eventSheetTimeSlotPrimaryKey.eventSheetId,
            date = this.eventSheetTimeSlotPrimaryKey.date,
            startTime = this.startTime,
            endTime = this.endTime
        )
    }
}

@Embeddable
data class EventSheetTimeSlotPrimaryKey(
    val eventSheetId: Long? = null,

    @Column(name = "date")
    val date: Date
) : Serializable