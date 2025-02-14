package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.EventSheetCodeWordRepository
import com.meetgom.backend.data.repository.EventSheetCodeRepository
import com.meetgom.backend.data.repository.EventSheetRepository
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.domain.model.event_sheet.EventSheet
import com.meetgom.backend.domain.model.event_sheet.EventSheetCode
import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.domain.model.participant.Participant
import com.meetgom.backend.domain.service.common.CommonEventSheetService
import com.meetgom.backend.domain.service.common.CommonParticipantService
import com.meetgom.backend.exception.exceptions.ParticipantExceptions
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.extends.atTimeZone
import com.meetgom.backend.utils.extends.sorted
import com.meetgom.backend.utils.extends.toTimeZone
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventSheetService(
    private val commonEventSheetService: CommonEventSheetService,
    private val commonParticipantService: CommonParticipantService,
    private val eventSheetRepository: EventSheetRepository,
    private val eventSheetCodeRepository: EventSheetCodeRepository,
    private val eventSheetCodeWordRepository: EventSheetCodeWordRepository
) {

    companion object {
        const val EVENT_SHEET_CODE_MAX_TRY_COUNT = 1000
    }

    @Transactional
    fun createEventSheet(
        name: String,
        description: String?,
        eventSheetType: EventSheetType,
        hostTimeZoneRegion: String,
        activeStartDateTime: LocalDateTime?,
        activeEndDateTime: LocalDateTime?,
        manualActive: Boolean?,
        eventSheetTimeSlots: List<EventSheetTimeSlot>,
        wordCount: Int,
        pinCode: String,
    ): EventSheet {
        val hostTimeZone =
            commonEventSheetService.findTimeZoneEntityByRegionWithException(hostTimeZoneRegion).toDomain()
        val eventSheetCode = createRandomEventSheetCode(wordCount = wordCount)
        val validEventSheetTimeSlots = validateEventSheetTimeSlots(eventSheetType, eventSheetTimeSlots)
        val eventSheetEntity = EventSheet(
            eventSheetCode = eventSheetCode,
            name = name,
            description = description,
            eventSheetType = eventSheetType,
            timeZone = hostTimeZone,
            hostTimeZone = hostTimeZone,
            activeStartDateTime = activeStartDateTime?.atTimeZone(hostTimeZone),
            activeEndDateTime = activeEndDateTime?.atTimeZone(hostTimeZone),
            eventSheetTimeSlots = validEventSheetTimeSlots,
            manualActive = manualActive,
            participants = emptyList()
        ).toEntity()
        return eventSheetRepository.save(eventSheetEntity).toDomain()
    }

    fun readEventSheetByEventSheetCode(
        eventSheetCode: String,
        region: String?,
    ): EventSheet {
        val eventSheet =
            commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCodeValue = eventSheetCode)
                .toDomain()
        val convertedEventSheet = commonEventSheetService.convertEventSheetTimeZone(eventSheet, region)
        return convertedEventSheet
    }

    @Transactional
    fun updateEventSheetByEventSheetCode(
        eventSheetCode: String,
        name: String?,
        description: String?,
        activeStartDateTime: LocalDateTime?,
        activeEndDateTime: LocalDateTime?,
        manualActive: Boolean?
    ): EventSheet {
        val eventSheetEntity =
            commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCodeValue = eventSheetCode)
        val hostTimeZone = eventSheetEntity.hostTimeZoneEntity.toDomain()
        val timeZone = eventSheetEntity.timeZoneEntity.toDomain()
        val activeStartZonedDateTime = activeStartDateTime?.atTimeZone(hostTimeZone)?.toTimeZone(timeZone)
        val activeEndZonedDateTime = activeEndDateTime?.atTimeZone(hostTimeZone)?.toTimeZone(timeZone)
        eventSheetEntity.update(
            name = name,
            description = description,
            activeStartDateTime = activeStartZonedDateTime,
            activeEndDateTime = activeEndZonedDateTime,
            manualActive = manualActive
        )
        return eventSheetRepository.save(eventSheetEntity).toDomain()
    }

    @Transactional
    fun deleteEventSheetByEventSheetCode(eventSheetCode: String): Boolean {
        commonEventSheetService.deleteEventSheetEntityByEventSheetCode(eventSheetCodeValue = eventSheetCode)
        commonParticipantService.deleteAnonymousUserEntityByEventSheetCode(eventSheetCode = eventSheetCode)
        return true
    }

    @Transactional
    fun readParticipantInEventSheet(
        eventSheetCode: String,
        participantId: Long,
        region: String?
    ): Pair<EventSheetType, Participant> {
        val eventSheet =
            commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCodeValue = eventSheetCode)
                .toDomain()
        val convertedEventSheet = commonEventSheetService.convertEventSheetTimeZone(eventSheet, region)
        val participant = convertedEventSheet.participants.find { it.id == participantId }
            ?: throw ParticipantExceptions.PARTICIPANT_NOT_FOUND.toException()
        return Pair(convertedEventSheet.eventSheetType, participant)
    }

    // MARK: - Private Methods
    private fun createRandomEventSheetCode(wordCount: Int = 3): EventSheetCode {
        for (i in 0 until EVENT_SHEET_CODE_MAX_TRY_COUNT) {
            val eventSheetCodeWords = eventSheetCodeWordRepository.findRandomEventSheetCodeWords(wordCount = wordCount)
            val eventSheetCodeValue = eventSheetCodeWords.joinToString(separator = "-") { it.word }
            if (eventSheetCodeRepository.findByEventSheetCode(eventSheetCodeValue) == null) {
                return EventSheetCode(
                    eventSheetCode = eventSheetCodeValue
                )
            }
        }
        throw EventSheetExceptions.MAX_EVENT_SHEET_CODES_REACHED.toException()
    }

    private fun validateEventSheetTimeSlots(
        eventSheetType: EventSheetType,
        eventSheetTimeSlots: List<EventSheetTimeSlot>
    ): List<EventSheetTimeSlot> {
        if (eventSheetType == EventSheetType.RECURRING_WEEKDAYS) {
            val dayOfWeeks = eventSheetTimeSlots.map { it.date.dayOfWeek }
            if (dayOfWeeks.distinct().size != dayOfWeeks.size)
                throw EventSheetExceptions.INVALID_DAY_OF_WEEKS.toException()
        }
        eventSheetTimeSlots.sorted(eventSheetType = eventSheetType)
            .forEach { eventSheetTimeSlot ->
                if (eventSheetTimeSlot.startTime.isAfter(eventSheetTimeSlot.endTime))
                    throw EventSheetExceptions.INVALID_EVENT_SHEET_TIME_SLOTS.toException()
            }
        return eventSheetTimeSlots
    }
}