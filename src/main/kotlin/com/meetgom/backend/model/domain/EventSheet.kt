package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.extends.alignTimeSlots
import com.meetgom.backend.utils.extends.toTimeZone
import java.time.ZonedDateTime

data class EventSheet(
    val id: Long? = null,
    val eventCode: EventCode,
    val name: String,
    val description: String?,
    val eventSheetType: EventSheetType,
    val hostTimeZone: TimeZone,
    val activeStartDateTime: ZonedDateTime?,
    val activeEndDateTime: ZonedDateTime?,
    val timeZone: TimeZone,
    val eventSheetTimeSlots: List<EventSheetTimeSlot>? = null,
    val manualActive: Boolean? = false,
    val createdAt: ZonedDateTime? = null,
    val updatedAt: ZonedDateTime? = null
) {
    private fun isActive(): Boolean {
        val now = ZonedDateTime.now()
        manualActive?.let {
            return it
        }
        return (this.activeStartDateTime?.isAfter(now) ?: true) &&
                (this.activeEndDateTime?.isBefore(now) ?: true)

    }

    fun convertTimeZone(to: TimeZone?): EventSheet {
        val timeZone = to ?: hostTimeZone
        val activeStartDateTime = this.activeStartDateTime?.toTimeZone(timeZone)
        val activeEndDateTime = this.activeEndDateTime?.toTimeZone(timeZone)
        val eventSheetTimeSlots = this.eventSheetTimeSlots?.map {
            it.convertTimeZone(
                from = this.timeZone,
                to = timeZone,
            )
        }?.flatten()?.alignTimeSlots()

        return EventSheet(
            id = this.id,
            eventCode = this.eventCode,
            name = this.name,
            description = this.description,
            eventSheetType = this.eventSheetType,
            hostTimeZone = this.hostTimeZone,
            activeStartDateTime = activeStartDateTime,
            activeEndDateTime = activeEndDateTime,
            eventSheetTimeSlots = eventSheetTimeSlots,
            manualActive = this.manualActive,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            timeZone = timeZone,
        )
    }

    private fun convertSystemDefaultTimeZone(): EventSheet {
        return this.convertTimeZone(TimeZone.defaultTimeZone)
    }

    // MARK: - Converters
    fun toEntity(): EventSheetEntity {
        val eventSheet = this.convertSystemDefaultTimeZone()
        val eventSheetEntity = EventSheetEntity(
            name = eventSheet.name,
            description = eventSheet.description,
            eventSheetType = eventSheet.eventSheetType,
            activeStartDateTime = eventSheet.activeStartDateTime,
            activeEndDateTime = eventSheet.activeEndDateTime,
            manualActive = eventSheet.manualActive,
            hostTimeZoneEntity = eventSheet.hostTimeZone.toEntity(),
            eventCode = eventSheet.eventCode.toEntity(),
        )
        val eventSheetTimeSlotEntities =
            eventSheet.eventSheetTimeSlots?.map { it.toEntity(eventSheetEntity = eventSheetEntity) }?.toMutableList()
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
            eventSheetType = this.eventSheetType,
            hostTimeZone = this.hostTimeZone.region,
            activeStartDateTime = this.activeStartDateTime?.toLocalDateTime(),
            activeEndDateTime = this.activeEndDateTime?.toLocalDateTime(),
            manualActive = this.manualActive,
            isActive = this.isActive(),
            eventSheetTimeSlots = this.eventSheetTimeSlots?.map { it.toResponse(hideDate = eventSheetType == EventSheetType.RECURRING_WEEKDAYS) }
                ?: listOf(),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            timeZone = this.timeZone.region
        )
    }
}