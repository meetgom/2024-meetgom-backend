package com.meetgom.backend.controller

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.http.request.PostEventSheetRequest
import com.meetgom.backend.model.http.request.PostRecurringWeekdaysEventSheetRequest
import com.meetgom.backend.model.http.request.PostSpecificDatesEventSheetRequest
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.service.EventSheetService
import com.meetgom.backend.utils.TimeUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

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
            eventSheetType = postEventSheetRequest.eventSheetType,
            hostTimeZoneRegion = postEventSheetRequest.timeZone,
            activeStartDateTime = postEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postEventSheetRequest.activeEndDateTime,
            manualActive = postEventSheetRequest.manualActive,
            eventSheetTimeSlots = postEventSheetRequest.eventSheetTimeSlots.map {
                EventSheetTimeSlot(
                    date = it.date,
                    startTime = TimeUtils.timeStringToLocalTime(it.startTime),
                    endTime = TimeUtils.timeStringToLocalTime(it.endTime)
                )
            },
            wordCount = postEventSheetRequest.wordCount ?: 3
        )
        return eventSheet.toResponse()
    }

    @PostMapping(path = ["specific-dates"])
    @Operation(summary = "create Specific Dates Event Sheet")
    fun postSpecificDatesEventSheet(@RequestBody postSpecificDatesEventSheetRequest: PostSpecificDatesEventSheetRequest): EventSheetResponse {
        val postEventSheetRequest = postSpecificDatesEventSheetRequest.toPostEventSheetRequest()
        return postEventSheet(postEventSheetRequest)
    }

    // MARK: - 이벤트 타입에 따른 이벤트 시트 생성
    @PostMapping(path = ["recurring-weekdays"])
    @Operation(summary = "create Recurring Weekdays Event Sheet")
    fun postRecurringWeekdaysEventSheet(@RequestBody postRecurringWeekdaysEventSheetRequest: PostRecurringWeekdaysEventSheetRequest): EventSheetResponse {
        val postEventSheetRequest = postRecurringWeekdaysEventSheetRequest.toPostEventSheetRequest()
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
}