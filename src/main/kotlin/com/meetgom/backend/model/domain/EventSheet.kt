package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.type.EventDateType
import com.meetgom.backend.utils.extends.addTimeZone
import com.meetgom.backend.utils.extends.toLocalDateTimeWithTimeZone
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class EventSheet(
    val id: Long? = null,
    val eventCode: EventCode,
    val name: String,
    val description: String?,
    val eventDateType: EventDateType,
    val hostTimeZone: TimeZone,
    val activeStartDateTime: LocalDateTime?,
    val activeEndDateTime: LocalDateTime?,
    val eventSheetTimeSlots: List<EventSheetTimeSlot>? = null,
    val manualActive: Boolean,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val timeZone: TimeZone,
) {
    private fun isActive(): Boolean {
        val now = ZonedDateTime.now()
        return (this.activeStartDateTime?.addTimeZone(timeZone)?.isBefore(now) ?: true) &&
                (this.activeEndDateTime?.addTimeZone(timeZone)?.isBefore(now) ?: true) &&
                manualActive
    }

    fun convertTimeZone(timeZone: TimeZone): EventSheet {
        val activeStartDateTime = this.activeStartDateTime?.addTimeZone(this.hostTimeZone)?.toLocalDateTimeWithTimeZone(timeZone)
        val activeEndDateTime = this.activeEndDateTime?.addTimeZone(this.hostTimeZone)?.toLocalDateTimeWithTimeZone(timeZone)
        val eventSheetTimeSlots = this.eventSheetTimeSlots?.map {
            it.convertTimeZone(
                hostTimeZone = this.hostTimeZone,
                timeZone = timeZone
            )
        }

        return EventSheet(
            id = this.id,
            eventCode = this.eventCode,
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            hostTimeZone = this.hostTimeZone,
            activeStartDateTime = activeStartDateTime,
            activeEndDateTime = activeEndDateTime,
            eventSheetTimeSlots = eventSheetTimeSlots,
            manualActive = this.manualActive,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            timeZone = timeZone
        )
    }

    // MARK: - Converters
    fun toEntity(): EventSheetEntity {
        val eventSheetEntity = EventSheetEntity(
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            activeStartDateTime = this.activeStartDateTime,
            activeEndDateTime = this.activeEndDateTime,
            manualActive = this.manualActive,
            hostTimeZoneEntity = this.hostTimeZone.toEntity(),
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
            isActive = this.isActive(),
            timeZone = this.timeZone.region,
            hostTimeZone = this.hostTimeZone.region,
            eventSheetTimeSlots = this.eventSheetTimeSlots?.map { it.toResponse() } ?: listOf()
        )
    }
}