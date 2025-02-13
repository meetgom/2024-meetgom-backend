package com.meetgom.backend.domain.model.event_sheet

import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.controller.http.response.EventSheetResponse
import com.meetgom.backend.controller.http.response.ParticipantTimeSlotTableResponse
import com.meetgom.backend.domain.model.participant.Participant
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.utils.TimeUtils
import com.meetgom.backend.utils.extends.sorted
import com.meetgom.backend.utils.extends.toTimeZone
import java.time.ZonedDateTime

data class EventSheet(
    val id: Long? = null,
    val eventSheetCode: EventSheetCode,
    val name: String,
    val description: String?,
    val eventSheetType: EventSheetType,
    val hostTimeZone: TimeZone,
    val activeStartDateTime: ZonedDateTime?,
    val activeEndDateTime: ZonedDateTime?,
    val timeZone: TimeZone,
    val eventSheetTimeSlots: List<EventSheetTimeSlot>,
    val manualActive: Boolean? = false,
    val createdAt: ZonedDateTime? = null,
    val updatedAt: ZonedDateTime? = null,
    val participants: List<Participant>,
) {
    fun isActive(): Boolean {
        val now = ZonedDateTime.now()
        manualActive?.let {
            return it
        }
        return (this.activeStartDateTime?.isAfter(now) ?: true) &&
                (this.activeEndDateTime?.isBefore(now) ?: true)

    }

    fun convertTimeZone(to: TimeZone? = null): EventSheet {
        val timeZone = to ?: hostTimeZone
        val activeStartDateTime = this.activeStartDateTime?.toTimeZone(timeZone)
        val activeEndDateTime = this.activeEndDateTime?.toTimeZone(timeZone)
        val eventSheetTimeSlots = this.eventSheetTimeSlots.map {
            it.convertTimeZone(
                from = this.timeZone,
                to = timeZone,
            )
        }.flatten().sorted(eventSheetType = eventSheetType)
        val participants = this.participants.map {
            it.convertTimeZone(to = timeZone, eventSheetType = eventSheetType)
        }

        return EventSheet(
            id = this.id,
            eventSheetCode = this.eventSheetCode,
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
            participants = participants,
        )
    }

    private fun convertSystemDefaultTimeZone(): EventSheet {
        return this.convertTimeZone(to = TimeZone.defaultTimeZone)
    }

    fun getMatchedTimeZone(region: String?): TimeZone? {
        return when (region) {
            this.hostTimeZone.region -> this.hostTimeZone
            this.timeZone.region -> this.timeZone
            else -> null
        }
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
            timeZoneEntity = eventSheet.timeZone.toEntity(),
            hostTimeZoneEntity = eventSheet.hostTimeZone.toEntity(),
            eventSheetCodeEntity = eventSheet.eventSheetCode.toEntity(),
            eventSheetTimeSlotEntities = mutableListOf(),
        )
        val eventSheetTimeSlotEntities =
            eventSheet.eventSheetTimeSlots.map { it.toEntity(eventSheetEntity = eventSheetEntity) }.toMutableList()
        eventSheetEntity.eventSheetTimeSlotEntities = eventSheetTimeSlotEntities
        return eventSheetEntity
    }

    fun toResponse(): EventSheetResponse {
        val hideDate = eventSheetType == EventSheetType.RECURRING_WEEKDAYS
        return EventSheetResponse(
            id = this.id,
            eventSheetCode = this.eventSheetCode.eventSheetCode,
            name = this.name,
            description = this.description,
            eventSheetType = this.eventSheetType,
            hostTimeZone = this.hostTimeZone.region,
            activeStartDateTime = this.activeStartDateTime?.toLocalDateTime(),
            activeEndDateTime = this.activeEndDateTime?.toLocalDateTime(),
            manualActive = this.manualActive,
            isActive = this.isActive(),
            eventSheetTimeSlots = this.eventSheetTimeSlots.map { it.toResponse(hideDate = hideDate) },
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            timeZone = this.timeZone.region,
            participant = participants.map { it.toResponse(hideDate = hideDate) },
            timeSlotTable = participantsToTimeSlotTableResponse(participants, hideDate = hideDate),
        )
    }

    //MARK: - Private Methods
    private fun participantsToTimeSlotTableResponse(
        participants: List<Participant>,
        hideDate: Boolean = false
    ): List<ParticipantTimeSlotTableResponse> {
        val timeSlots = participants.flatMap { it.availableTimeSlots }
        val dateToTimes = timeSlots
            .groupBy { it.date }
            .mapValues { (_, slots) ->
                slots.flatMap { listOf(it.startTime, it.endTime) }
                    .distinct()
                    .sorted()
            }
        return dateToTimes.flatMap { (date, timePoints) ->
            timePoints.zipWithNext { start, end ->
                val matchedParticipants = participants.filter { participant ->
                    participant.availableTimeSlots.any {
                        it.date == date && it.startTime <= start && it.endTime >= end
                    }
                }
                ParticipantTimeSlotTableResponse(
                    date = if (hideDate) null else date,
                    dayOfWeek = date.dayOfWeek.name,
                    startTime = TimeUtils.localTimeToTimeString(start),
                    endTime = TimeUtils.localTimeToTimeString(end),
                    availableParticipantsRatio = (matchedParticipants.size.toDouble() / participants.size) * 100,
                    availableParticipantsCount = matchedParticipants.size,
                    participants = matchedParticipants.map { it.user.userName }
                )
            }
        }
    }

}