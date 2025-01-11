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
        const val EVENT_CODE_MAX_TRY_COUNT = 100
    }

    @Transactional
    fun createEventSheet(
        name: String,
        description: String?,
        eventDateType: EventDateType,
        activeStartDateTime: LocalDateTime?,
        activeEndDateTime: LocalDateTime?,
        isActive: Boolean,
        eventSheetTimeSlots: List<EventSheetTimeSlot>,
        timeZoneRegion: String,
        wordCount: Int
    ): EventSheet {
        val timeZone = timeZoneRepository.findByRegion(timeZoneRegion)?.toDomain() ?: throw Exception("Time zone not found")
        val eventCode = createEventSheetEventCode(wordCount = wordCount)
        val eventSheetEntity = EventSheet(
            name = name,
            description = description,
            eventDateType = eventDateType,
            activeStartDateTime = activeStartDateTime,
            activeEndDateTime = activeEndDateTime,
            manualActive = isActive,
            eventCode = eventCode,
            timeZone = timeZone,
            eventSheetTimeSlots = eventSheetTimeSlots
        ).toEntity()
        val savedEventSheet = eventSheetRepository.save(eventSheetEntity).toDomain()
        return savedEventSheet
    }

    fun readEventSheetByEventCode(eventCode: String): EventSheet {
        val eventSheetEntity = eventSheetRepository.findByEventCode(eventCode) ?: throw Exception("Event sheet not found")
        return eventSheetEntity.toDomain()
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
}