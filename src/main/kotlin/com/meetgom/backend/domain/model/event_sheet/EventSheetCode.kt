package com.meetgom.backend.domain.model.event_sheet

import com.meetgom.backend.data.entity.event_sheet.EventCodeEntity

data class EventSheetCode(
    val eventCode: String,
    val eventSheet: EventSheet? = null,
) {
    fun toEntity(): EventCodeEntity {
        return EventCodeEntity(
            eventCode = this.eventCode,
        )
    }
}