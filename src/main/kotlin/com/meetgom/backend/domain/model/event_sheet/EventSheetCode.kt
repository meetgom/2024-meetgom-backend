package com.meetgom.backend.domain.model.event_sheet

import com.meetgom.backend.data.entity.event_sheet.EventSheetCodeEntity

data class EventSheetCode(
    val eventSheetCode: String,
    val eventSheet: EventSheet? = null,
) {
    fun toEntity(): EventSheetCodeEntity {
        return EventSheetCodeEntity(
            eventSheetCode = this.eventSheetCode,
        )
    }
}