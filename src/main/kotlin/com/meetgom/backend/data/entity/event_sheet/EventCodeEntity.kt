package com.meetgom.backend.data.entity.event_sheet

import com.meetgom.backend.domain.model.event_sheet.EventSheetCode
import jakarta.persistence.*


@Entity(name = "event_code")
class EventCodeEntity(
    @Id
    @Column(name = "event_code", length = 256)
    val eventCode: String,
) {
    fun toDomain(): EventSheetCode {
        return EventSheetCode(
            eventCode = this.eventCode,
        )
    }
}