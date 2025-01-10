package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.type.EventDateType
import java.util.Date

data class EventSheet(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val eventDateType: EventDateType,
    val activeStartDate: Date?,
    val activeEndDate: Date?,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val isActive: Boolean,
    val timeZone: TimeZone,
    val eventCode: EventCode,
    val eventSheetTimeSlots: List<EventSheetTimeSlot>? = null
) {
    fun toEntity(): EventSheetEntity {
        var eventSheetEntity = EventSheetEntity(
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            activeStartDate = this.activeStartDate,
            activeEndDate = this.activeEndDate,
            isActive = this.isActive,
            timeZoneEntity = this.timeZone.toEntity(),
            eventCode = this.eventCode.toEntity()
        )
        val eventSheetTimeSlotEntities =
            this.eventSheetTimeSlots?.map { it.toEntity(eventSheetEntity = eventSheetEntity) }?.toMutableList()
                ?: mutableListOf()
        eventSheetEntity.eventSheetTimeSlotEntities = eventSheetTimeSlotEntities
        return eventSheetEntity
    }

    fun toResponse(): EventSheetResponse {
        return EventSheetResponse(
            id = this.id,
            eventCode = this.eventCode.eventCode,
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            activeStartDate = this.activeStartDate,
            activeEndDate = this.activeEndDate,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            isActive = this.isActive,
            timeZone = this.timeZone.toResponse(),
            eventSheetTimeSlots = this.eventSheetTimeSlots?.map { it.toResponse() } ?: listOf()
        )
    }
}