package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.EventCodeWordRepository
import com.meetgom.backend.data.repository.EventSheetCodeRepository
import com.meetgom.backend.data.repository.EventSheetRepository
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.domain.model.event_sheet.EventSheet
import com.meetgom.backend.domain.model.event_sheet.EventSheetCode
import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.domain.service.common.CommonEventSheetService
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.extends.atTimeZone
import com.meetgom.backend.utils.extends.sorted
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventSheetService(
    private val commonEventSheetService: CommonEventSheetService,
    private val eventSheetRepository: EventSheetRepository,
    private val eventSheetCodeRepository: EventSheetCodeRepository,
    private val eventSheetCodeWordRepository: EventCodeWordRepository
) {

    companion object {
        const val EVENT_CODE_MAX_TRY_COUNT = 1000
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
        val eventCode = createRandomEventSheetCode(wordCount = wordCount)
        val validEventSheetTimeSlots = validateEventSheetTimeSlots(eventSheetType, eventSheetTimeSlots)
        val eventSheet = EventSheet(
            eventSheetCode = eventCode,
            name = name,
            description = description,
            eventSheetType = eventSheetType,
            hostTimeZone = hostTimeZone,
            activeStartDateTime = activeStartDateTime?.atTimeZone(hostTimeZone),
            activeEndDateTime = activeEndDateTime?.atTimeZone(hostTimeZone),
            eventSheetTimeSlots = validEventSheetTimeSlots,
            manualActive = manualActive,
            timeZone = hostTimeZone,
            participants = emptyList()
        ).toEntity()
        return eventSheetRepository.save(eventSheet).toDomain()
    }

    fun readEventSheetByEventCode(
        eventSheetCode: String,
        region: String?,
    ): EventSheet {
        val eventSheet =
            commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCodeValue = eventSheetCode)
                .toDomain()
        val convertedEventSheet = commonEventSheetService.convertEventSheetTimeZone(eventSheet, region)
        return convertedEventSheet
    }

    // MARK: - Private Methods
    private fun createRandomEventSheetCode(wordCount: Int = 3): EventSheetCode {
        for (i in 0 until EVENT_CODE_MAX_TRY_COUNT) {
            val eventCodeWords = eventSheetCodeWordRepository.findRandomEventCodeWords(wordCount = wordCount)
            val eventCodeValue = eventCodeWords.joinToString(separator = "-") { it.word }
            if (eventSheetCodeRepository.findByEventSheetCode(eventCodeValue) == null) {
                return EventSheetCode(
                    eventCode = eventCodeValue
                )
            }
        }
        throw EventSheetExceptions.MAX_EVENT_CODES_REACHED.toException()
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