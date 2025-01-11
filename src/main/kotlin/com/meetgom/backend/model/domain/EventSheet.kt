package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.type.EventDateType
import java.time.LocalDateTime

data class EventSheet(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val eventDateType: EventDateType,
    val activeStartDateTime: LocalDateTime?,
    val activeEndDateTime: LocalDateTime?,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val manualActive: Boolean,
    val timeZone: TimeZone,
    val eventCode: EventCode,
    val eventSheetTimeSlots: List<EventSheetTimeSlot>? = null
) {
    fun toEntity(): EventSheetEntity {
        val eventSheetEntity = EventSheetEntity(
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            activeStartDateTime = this.activeStartDateTime,
            activeEndDateTime = this.activeEndDateTime,
            manualActive = this.manualActive,
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
            activeStartDateTime = this.activeStartDateTime,
            activeEndDateTime = this.activeEndDateTime,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            isActive = this.manualActive,
            timeZone = this.timeZone.region,
            eventSheetTimeSlots = this.eventSheetTimeSlots?.map { it.toResponse() } ?: listOf()
        )
    }
}