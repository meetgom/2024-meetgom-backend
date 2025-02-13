package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.ParticipantRepository
import com.meetgom.backend.data.repository.UserRepository
import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.domain.model.event_sheet.EventSheet
import com.meetgom.backend.domain.model.participant.Participant
import com.meetgom.backend.domain.model.participant.ParticipantAvailableTimeSlot
import com.meetgom.backend.domain.model.participant.TempParticipantAvailableTimeSlot
import com.meetgom.backend.domain.service.common.CommonEventSheetService
import com.meetgom.backend.domain.service.common.CommonParticipantService
import com.meetgom.backend.domain.service.common.CommonUserService
import com.meetgom.backend.exception.exceptions.ParticipantExceptions
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.type.ParticipantRoleType
import com.meetgom.backend.type.UserType
import com.meetgom.backend.utils.extends.sorted
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ParticipantService(
    private val commonEventSheetService: CommonEventSheetService,
    private val commonParticipantService: CommonParticipantService,
    private val commonUserService: CommonUserService,
    private val participantRepository: ParticipantRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createAnonymousParticipant(
        eventSheetCode: String,
        userName: String,
        password: String,
        region: String?,
        tempAvailableTimeSlots: List<TempParticipantAvailableTimeSlot>,
    ): Participant {
        val eventSheetEntity = commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCode)
        val eventSheet = eventSheetEntity.toDomain()
        val timeZone =
            if (region == null) eventSheet.hostTimeZone else commonEventSheetService.findTimeZoneEntityByRegionWithException(
                region = region
            ).toDomain()
        val anonymousUserEntity = commonUserService.createUser(
            userType = UserType.ANONYMOUS,
            userName = userName,
            password = password
        )
        val role = ParticipantRoleType.PARTICIPANT
        val participantRoleEntity = commonParticipantService.findPariticipantRoleEntityByRoleTypeWithException(role)
        val availableTimeSlots = validateTemporaryAvailableTimeSlots(
            eventSheet = eventSheet,
            timeZone = timeZone,
            tempAvailableTimeSlots = tempAvailableTimeSlots
        )
        val participantEntity = Participant(
            eventSheetCode = eventSheetCode,
            user = anonymousUserEntity.toDomain(),
            role = role,
            timeZone = timeZone,
            availableTimeSlots = availableTimeSlots
        ).toEntity(
            eventSheetEntity = eventSheetEntity,
            userEntity = anonymousUserEntity,
            participantRoleEntity = participantRoleEntity
        )
        return participantRepository.save(participantEntity).toDomain()
    }

    fun createStandardParticipant(
        eventSheetCode: String,
        region: String?,
        tempAvailableTimeSlots: List<TempParticipantAvailableTimeSlot>,
    ): Participant {
        val eventSheetEntity = commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCode)
        val eventSheet = eventSheetEntity.toDomain()
        val timeZone =
            if (region == null) eventSheet.hostTimeZone else commonEventSheetService.findTimeZoneEntityByRegionWithException(
                region = region
            ).toDomain()

        // FIXME: - jwt token으로 user 식별 필요
        val userEntity = userRepository.findById(1).getOrNull()
            ?: throw ParticipantExceptions.USER_NOT_FOUND.toException()
        val user = userEntity.toDomain()
        val role = ParticipantRoleType.PARTICIPANT
        val participantRoleEntity = commonParticipantService.findPariticipantRoleEntityByRoleTypeWithException(role)
        val availableTimeSlots = validateTemporaryAvailableTimeSlots(
            eventSheet = eventSheet,
            timeZone = timeZone,
            tempAvailableTimeSlots = tempAvailableTimeSlots
        )

        val participantEntity = Participant(
            eventSheetCode = eventSheetCode,
            user = user,
            role = role,
            timeZone = timeZone,
            availableTimeSlots = availableTimeSlots
        ).toEntity(
            eventSheetEntity = eventSheetEntity,
            userEntity = userEntity,
            participantRoleEntity = participantRoleEntity
        )
        return participantRepository.save(participantEntity).toDomain()
    }

    // MARK: - Private Methods
    private fun validateTemporaryAvailableTimeSlots(
        eventSheet: EventSheet,
        timeZone: TimeZone,
        tempAvailableTimeSlots: List<TempParticipantAvailableTimeSlot>
    ): List<ParticipantAvailableTimeSlot> {
        val convertedEventSheet = eventSheet.convertTimeZone(timeZone)
        val availableTimeSlots = when (convertedEventSheet.eventSheetType) {
            EventSheetType.SPECIFIC_DATES -> {
                tempAvailableTimeSlots.map {
                    if (it.date == null) throw ParticipantExceptions.AVAILABLE_TIME_SLOT_DATE_NOT_FOUND.toException()
                    ParticipantAvailableTimeSlot(
                        date = it.date,
                        startTime = it.startTime,
                        endTime = it.endTime
                    )
                }
            }

            EventSheetType.RECURRING_WEEKDAYS -> {
                val dayOfWeekDateMap =
                    convertedEventSheet.eventSheetTimeSlots.fold(EnumMap<DayOfWeek, LocalDate>(DayOfWeek::class.java)) { acc, eventSheetTimeSlot ->
                        acc[eventSheetTimeSlot.date.dayOfWeek] = eventSheetTimeSlot.date
                        acc
                    }
                tempAvailableTimeSlots.map {
                    if (it.dayOfWeek == null) throw ParticipantExceptions.AVAILABLE_TIME_SLOT_DAY_OF_WEEK_NOT_FOUND.toException()
                    val date = dayOfWeekDateMap[it.dayOfWeek]
                        ?: throw ParticipantExceptions.AVAILABLE_TIME_SLOT_INVALID_DAY_OF_WEEK.toException()
                    ParticipantAvailableTimeSlot(
                        date = date,
                        startTime = it.startTime,
                        endTime = it.endTime
                    )
                }
            }
        }.sorted(eventSheetType = convertedEventSheet.eventSheetType)
        availableTimeSlots.forEach { timeSlot ->
            val res = convertedEventSheet.eventSheetTimeSlots.any { it.contains(timeSlot) }
            if (!res) throw ParticipantExceptions.AVAILABLE_TIME_SLOT_INVALID_TIME_SLOTS.toException()
        }
        return availableTimeSlots
    }
}