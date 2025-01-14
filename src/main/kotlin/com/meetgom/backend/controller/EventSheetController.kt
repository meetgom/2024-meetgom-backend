package com.meetgom.backend.controller

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.http.request.PostEventSheetRequest
import com.meetgom.backend.model.http.request.PostEventSheetTimeSlotRequest
import com.meetgom.backend.model.http.request.PostRecurringWeekdaysEventSheetRequest
import com.meetgom.backend.model.http.request.PostSpecificDatesEventSheetRequest
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.service.EventSheetService
import com.meetgom.backend.type.EventDateType
import com.meetgom.backend.utils.TimeUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.time.LocalTime

@Tag(name = "Event Sheet", description = "")
@RestController
@RequestMapping(path = ["/v1/event-sheet"])
class EventSheetController(private val eventSheetService: EventSheetService) {
    @PostMapping(path = [""])
    @Operation(summary = "create Event Sheet")
    fun postEventSheet(@RequestBody postEventSheetRequest: PostEventSheetRequest): EventSheetResponse {
        val eventSheet = eventSheetService.createEventSheet(
            name = postEventSheetRequest.name,
            description = postEventSheetRequest.description,
            eventDateType = postEventSheetRequest.eventDateType,
            hostTimeZoneRegion = postEventSheetRequest.timeZone,
            activeStartDateTime = postEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postEventSheetRequest.activeEndDateTime,
            manualActive = postEventSheetRequest.manualActive ?: true,
            eventSheetTimeSlots = postEventSheetRequest.eventSheetTimeSlots.map {
                EventSheetTimeSlot(
                    date = it.date,
                    startTime = TimeUtils.timeStringToLocalTime(it.startTime),
                    endTime = TimeUtils.timeStringToLocalTime(
                        it.endTime,
                        allowMidnight = false
                    )
                )
            },
            wordCount = postEventSheetRequest.wordCount ?: 3
        )
        return eventSheet.toResponse()
    }

    @PostMapping(path = ["specific-dates"])
    @Operation(summary = "create Specific Dates Event Sheet")
    fun postSpecificDatesEventSheet(@RequestBody postSpecificDatesEventSheetRequest: PostSpecificDatesEventSheetRequest): EventSheetResponse {
        val eventSheetTimeSlotsRequest = postSpecificDatesEventSheetRequest.specificDates.map { date ->
            PostEventSheetTimeSlotRequest(
                date = date,
                startTime = postSpecificDatesEventSheetRequest.startTime,
                endTime = postSpecificDatesEventSheetRequest.endTime
            )
        }
        val postEventSheetRequest = PostEventSheetRequest(
            name = postSpecificDatesEventSheetRequest.name,
            description = postSpecificDatesEventSheetRequest.description,
            eventDateType = EventDateType.SPECIFIC_DATES,
            timeZone = postSpecificDatesEventSheetRequest.timeZone,
            activeStartDateTime = postSpecificDatesEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postSpecificDatesEventSheetRequest.activeEndDateTime,
            manualActive = postSpecificDatesEventSheetRequest.manualActive,
            eventSheetTimeSlots = eventSheetTimeSlotsRequest,
            wordCount = postSpecificDatesEventSheetRequest.wordCount
        )
        return postEventSheet(postEventSheetRequest)
    }

    @GetMapping(path = ["/{eventCode}"])
    @Operation(summary = "read Event Sheet")
    fun getEventSheetByEventCode(
        @PathVariable eventCode: String,
        @RequestParam region: String?,
        @RequestParam key: String?
    ): EventSheetResponse {
        val eventSheet = eventSheetService.readEventSheetByEventCode(
            eventCode = eventCode,
            region = region,
            key = key
        )
        return eventSheet.toResponse()
    }


    // MARK: - 이벤트 타입에 따른 이벤트 시트 생성
    @PostMapping(path = ["recurring-weekdays"])
    @Operation(summary = "create Recurring Weekdays Event Sheet")
    fun postRecurringWeekdaysEventSheet(@RequestBody postRecurringWeekdaysEventSheetRequest: PostRecurringWeekdaysEventSheetRequest): EventSheetResponse {
        val eventSheetTimeSlotsRequest = postRecurringWeekdaysEventSheetRequest.recurringWeekdays.map {
            val date = TimeUtils.getClosestDayOfWeek(it)
            PostEventSheetTimeSlotRequest(
                date = date,
                startTime = postRecurringWeekdaysEventSheetRequest.startTime,
                endTime = postRecurringWeekdaysEventSheetRequest.endTime
            )
        }
        val postEventSheetRequest = PostEventSheetRequest(
            name = postRecurringWeekdaysEventSheetRequest.name,
            description = postRecurringWeekdaysEventSheetRequest.description,
            eventDateType = EventDateType.RECURRING_WEEKDAYS,
            timeZone = postRecurringWeekdaysEventSheetRequest.timeZone,
            activeStartDateTime = postRecurringWeekdaysEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postRecurringWeekdaysEventSheetRequest.activeEndDateTime,
            manualActive = postRecurringWeekdaysEventSheetRequest.manualActive,
            eventSheetTimeSlots = eventSheetTimeSlotsRequest,
            wordCount = postRecurringWeekdaysEventSheetRequest.wordCount
        )
        return postEventSheet(postEventSheetRequest)
    }
}