package com.meetgom.backend

import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetTimeSlotEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.data.repository.EventSheetRepository
import com.meetgom.backend.data.repository.TimeZoneRepository
import com.meetgom.backend.domain.model.event_sheet.EventSheetCode
import com.meetgom.backend.domain.service.EventSheetService
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.TimeUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
class EventSheetTests @Autowired constructor(
    private val eventSheetRepository: EventSheetRepository,
    private val timeZoneRepository: TimeZoneRepository,
) {
    @Test
    fun saveTest() {
        val timeZone = timeZoneRepository.findByRegion("Asia/Seoul")!!
        val eventSheetTimeSlotEntity = EventSheetTimeSlotEntity(
            eventSheetTimeSlotPrimaryKey = EventSheetTimeSlotPrimaryKey(
                date = LocalDate.now(),
                startTime = LocalTime.MIN,
            ),
            endTime = TimeUtils.MAX_LOCAL_TIME,
        )

        val eventSheetEntity = EventSheetEntity(
            eventSheetCodeEntity = EventSheetCode("test3").toEntity(),
            name = "test",
            description = "test",
            timeZoneEntity = timeZone,
            hostTimeZoneEntity = timeZone,
            manualActive = true,
            eventSheetType = EventSheetType.RECURRING_WEEKDAYS,
            activeStartDateTime = null,
            activeEndDateTime = null,
            eventSheetTimeSlotEntities = mutableListOf(),
        )
        eventSheetTimeSlotEntity.eventSheetEntity = eventSheetEntity
        eventSheetEntity.eventSheetTimeSlotEntities.add(eventSheetTimeSlotEntity)
        val vvv = eventSheetRepository.save(eventSheetEntity)
    }
}