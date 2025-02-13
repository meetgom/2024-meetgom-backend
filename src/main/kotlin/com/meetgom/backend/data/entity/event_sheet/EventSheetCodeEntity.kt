package com.meetgom.backend.data.entity.event_sheet

import com.meetgom.backend.domain.model.event_sheet.EventSheetCode
import jakarta.persistence.*


@Entity(name = "event_sheet_code")
class EventSheetCodeEntity(
    @Id
    @Column(name = "event_sheet_code", length = 256)
    val eventSheetCode: String,
) {
    fun toDomain(): EventSheetCode {
        return EventSheetCode(
            eventSheetCode = this.eventSheetCode,
        )
    }
}