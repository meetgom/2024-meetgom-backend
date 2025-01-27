package com.meetgom.backend.domain.model.event_sheet

import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.event_sheet.EventCodeEntity

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