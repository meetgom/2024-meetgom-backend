package com.meetgom.backend

import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetTimeSlotEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.data.repository.EventSheetRepository
import com.meetgom.backend.data.repository.TimeZoneRepository
import com.meetgom.backend.domain.model.event_sheet.EventSheetCode
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.utils.TimeUtils
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

}