package com.meetgom.backend.service

import com.meetgom.backend.exception.*
import com.meetgom.backend.model.domain.EventSheet
import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.domain.EventCode
import com.meetgom.backend.repository.EventSheetRepository
import com.meetgom.backend.repository.EventCodeRepository
import com.meetgom.backend.repository.EventCodeWordRepository
import com.meetgom.backend.repository.TimeZoneRepository
import com.meetgom.backend.security.EventCodeSecurity
import com.meetgom.backend.security.Security
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
            timeZoneRepository.findByRegion(hostTimeZoneRegion)?.toDomain() ?: throw TimeZoneNotFoundException()
        val eventCode = createEventSheetEventCode(pinCode = pinCode, wordCount = wordCount)
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
            eventSheetRepository.findByEventCode(eventCode)?.toDomain() ?: throw EventSheetNotFoundException()
        val timeZone = region.let {
            if (it.isNullOrEmpty() || it == eventSheet.hostTimeZone.region) eventSheet.hostTimeZone
            else if (it == eventSheet.timeZone.region) eventSheet.timeZone
            else timeZoneRepository.findByRegion(it)?.toDomain() ?: throw TimeZoneNotFoundException()
        }
        if (pinCode != null) {
            if (!EventCodeSecurity.checkPinCode(eventSheet.eventCode, pinCode))
                throw InvalidPinCodeException()
        }
        val convertedEventSheet = eventSheet.convertTimeZone(timeZone)
        return convertedEventSheet
    }

    // MARK: - Private Functions
    private fun createEventSheetEventCode(pinCode: String, wordCount: Int): EventCode {
        for (i in 0 until EVENT_CODE_MAX_TRY_COUNT) {
            val eventCodeWords = eventCodeWordRepository.getRandomEventCodeWords(wordCount = wordCount)
            val eventCodeValue = eventCodeWords.joinToString(separator = "-") { it.word }
            if (!eventCodeRepository.existsByEventCode(eventCodeValue)) {
                return EventCodeSecurity.generateEventCodeWithEncryptedPinCode(eventCodeValue, pinCode)
            }
        }
        throw MaxEventCodesReachedException()
    }

    private fun eventSheetTimeSlotsValidationCheck(
        eventSheetType: EventSheetType,
        eventSheetTimeSlots: List<EventSheetTimeSlot>
    ): List<EventSheetTimeSlot> {
        if (eventSheetType == EventSheetType.RECURRING_WEEKDAYS) {
            val dayOfWeeks = eventSheetTimeSlots.map { it.date.dayOfWeek }
            if (dayOfWeeks.distinct().size != dayOfWeeks.size)
                throw InvalidDayOfWeeksException()
        }
        eventSheetTimeSlots.sorted()
            .forEach { eventSheetTimeSlot ->
                if (eventSheetTimeSlot.startTime.isAfter(eventSheetTimeSlot.endTime))
                    throw InvalidEventSheetTimeSlotsException()
            }
        return eventSheetTimeSlots
    }
}