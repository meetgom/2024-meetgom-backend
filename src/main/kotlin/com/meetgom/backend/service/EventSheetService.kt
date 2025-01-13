package com.meetgom.backend.service

import com.meetgom.backend.entity.EventCodeWordEntity
import com.meetgom.backend.model.domain.EventSheet
import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.domain.EventCode
import com.meetgom.backend.repository.EventSheetRepository
import com.meetgom.backend.repository.EventCodeRepository
import com.meetgom.backend.repository.EventCodeWordRepository
import com.meetgom.backend.repository.TimeZoneRepository
import com.meetgom.backend.type.EventDateType
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
        eventDateType: EventDateType,
        hostTimeZoneRegion: String,
        activeStartDateTime: LocalDateTime?,
        activeEndDateTime: LocalDateTime?,
        manualActive: Boolean,
        eventSheetTimeSlots: List<EventSheetTimeSlot>,
        wordCount: Int
    ): EventSheet {
        val hostTimeZone =
            timeZoneRepository.findByRegion(hostTimeZoneRegion)?.toDomain() ?: throw Exception("Time zone not found")
        val eventCode = createEventSheetEventCode(wordCount = wordCount)
        if (eventSheetTimeSlotsValidationCheck(eventSheetTimeSlots).not())
            throw Exception("Event sheet time slots validation failed")
        val eventSheetEntity = EventSheet(
            eventCode = eventCode,
            name = name,
            description = description,
            eventDateType = eventDateType,
            hostTimeZone = hostTimeZone,
            activeStartDateTime = activeStartDateTime,
            activeEndDateTime = activeEndDateTime,
            eventSheetTimeSlots = eventSheetTimeSlots,
            manualActive = manualActive,
            timeZone = hostTimeZone
        )
            .toEntity()
        val savedEventSheet = eventSheetRepository.save(eventSheetEntity)
            .toDomain()
        return savedEventSheet
    }

    fun readEventSheetByEventCode(
        eventCode: String,
        region: String?,
        key: String?
    ): EventSheet {
        val eventSheet =
            eventSheetRepository.findByEventCode(eventCode)?.toDomain() ?: throw Exception("Event sheet not found")
        if (eventSheet.hostTimeZone.region == region) return eventSheet

        val timeZone = region?.let {
            if (eventSheet.timeZone.region == it) eventSheet.timeZone
            else timeZoneRepository.findByRegion(it)?.toDomain() ?: throw Exception("Time zone not found")
        } ?: eventSheet.hostTimeZone

        val convertedEventSheet = eventSheet.convertTimeZone(timeZone)

        return convertedEventSheet
    }

    // MARK: - Private Functions
    private fun createEventSheetEventCode(wordCount: Int): EventCode {
        var eventCodeWords: List<EventCodeWordEntity> = listOf()
        var newEventCode: String = ""
        for (i in 0 until EVENT_CODE_MAX_TRY_COUNT) {
            eventCodeWords = eventCodeWordRepository.getRandomEventCodeWords(wordCount = wordCount)
            newEventCode = eventCodeWords.joinToString("-") { it.word }
            if (!eventCodeRepository.existsByEventCode(newEventCode))
                return EventCode(newEventCode)
        }
        throw Exception("Failed to create event code")
    }

    private fun eventSheetTimeSlotsValidationCheck(eventSheetTimeSlots: List<EventSheetTimeSlot>): Boolean {
        eventSheetTimeSlots.sorted()
            .forEachIndexed { index, eventSheetTimeSlot ->
                if (index == 0) return@forEachIndexed
                if (eventSheetTimeSlot.startDateTime.isBefore(eventSheetTimeSlots[index - 1].endDateTime))
                    return false
            }
        return true
    }
}