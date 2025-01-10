package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.entity.EventCodeEntity

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