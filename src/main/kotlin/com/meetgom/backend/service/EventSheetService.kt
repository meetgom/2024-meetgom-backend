package com.meetgom.backend.service

import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.model.domain.event_sheet.EventSheet
import com.meetgom.backend.model.domain.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.model.domain.event_sheet.EventCode
import com.meetgom.backend.repository.EventSheetRepository
import com.meetgom.backend.repository.EventCodeRepository
import com.meetgom.backend.repository.EventCodeWordRepository
import com.meetgom.backend.repository.TimeZoneRepository
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.extends.atTimeZone
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventSheetService(
    private val eventCodeRepository: EventCodeRepository,
    private val eventCodeWordRepository: EventCodeWordRepository,
    private val eventSheetRepository: EventSheetRepository,
    private val timeZoneRepository: TimeZoneRepository
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
            timeZoneRepository.findByRegion(hostTimeZoneRegion)?.toDomain()
                ?: throw EventSheetExceptions.EVENT_SHEET_NOT_FOUND.toException()
        val eventCode = createEventSheetEventCode(wordCount = wordCount)
        // FIXME: - IF PIN CODE IS NOT NULL AND NOT USER, CREATE ANONYMOUS USER
        val validEventSheetTimeSlots = eventSheetTimeSlotsValidationCheck(eventSheetType, eventSheetTimeSlots)
        val eventSheetEntity = EventSheet(
            eventCode = eventCode,
            name = name,
            description = description,
            eventSheetType = eventSheetType,
            hostTimeZone = hostTimeZone,
            activeStartDateTime = activeStartDateTime?.atTimeZone(hostTimeZone),
            activeEndDateTime = activeEndDateTime?.atTimeZone(hostTimeZone),
            eventSheetTimeSlots = validEventSheetTimeSlots,
            manualActive = manualActive,
            timeZone = hostTimeZone,
        )
            .toEntity()
        val savedEventSheet = eventSheetRepository.save(eventSheetEntity)
            .toDomain()
        return savedEventSheet
    }

    fun readEventSheetByEventCode(
        eventCode: String,
        region: String?,
        pinCode: String?
    ): EventSheet {
        val eventSheet =
            eventSheetRepository.findByEventCode(eventCode)?.toDomain()
                ?: throw EventSheetExceptions.EVENT_SHEET_NOT_FOUND.toException()
        val timeZone = region.let {
            if (it.isNullOrEmpty() || it == eventSheet.hostTimeZone.region) eventSheet.hostTimeZone
            else if (it == eventSheet.timeZone.region) eventSheet.timeZone
            else timeZoneRepository.findByRegion(it)?.toDomain()
                ?: throw EventSheetExceptions.TIME_ZONE_NOT_FOUND.toException()
        }
        val convertedEventSheet = eventSheet.convertTimeZone(timeZone)
        return convertedEventSheet
    }

    // MARK: - Private Functions
    private fun createEventSheetEventCode(wordCount: Int): EventCode {
        for (i in 0 until EVENT_CODE_MAX_TRY_COUNT) {
            val eventCodeWords = eventCodeWordRepository.getRandomEventCodeWords(wordCount = wordCount)
            val eventCodeValue = eventCodeWords.joinToString(separator = "-") { it.word }
            if (!eventCodeRepository.existsByEventCode(eventCodeValue)) {
                return EventCode(eventCode = eventCodeValue)
            }
        }
        throw EventSheetExceptions.MAX_EVENT_CODES_REACHED.toException()
    }

    private fun eventSheetTimeSlotsValidationCheck(
        eventSheetType: EventSheetType,
        eventSheetTimeSlots: List<EventSheetTimeSlot>
    ): List<EventSheetTimeSlot> {
        if (eventSheetType == EventSheetType.RECURRING_WEEKDAYS) {
            val dayOfWeeks = eventSheetTimeSlots.map { it.date.dayOfWeek }
            if (dayOfWeeks.distinct().size != dayOfWeeks.size)
                throw EventSheetExceptions.INVALID_DAY_OF_WEEKS.toException()
        }
        eventSheetTimeSlots.sorted()
            .forEach { eventSheetTimeSlot ->
                if (eventSheetTimeSlot.startTime.isAfter(eventSheetTimeSlot.endTime))
                    throw EventSheetExceptions.INVALID_EVENT_SHEET_TIME_SLOTS.toException()
            }
        return eventSheetTimeSlots
    }
}