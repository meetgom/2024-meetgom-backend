package com.meetgom.backend.model.domain.event_sheet

import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.entity.event_sheet.EventCodeEntity

data class EventCode(
    val eventCode: String,
    val eventSheetId: Long? = null,
) {
    fun toEntity(eventSheetEntity: EventSheetEntity? = null): EventCodeEntity {
        return EventCodeEntity(
            eventCode = this.eventCode,
            eventSheetEntity = eventSheetEntity
        )
    }
}