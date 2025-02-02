package com.meetgom.backend.controller

import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.controller.http.HttpResponse
import com.meetgom.backend.controller.http.request.PostEventSheetRequest
import com.meetgom.backend.controller.http.request.PostRecurringWeekdaysEventSheetRequest
import com.meetgom.backend.controller.http.request.PostSpecificDatesEventSheetRequest
import com.meetgom.backend.controller.http.response.EventSheetResponse
import com.meetgom.backend.domain.service.EventSheetService
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
    fun postEventSheet(@RequestBody postEventSheetRequest: PostEventSheetRequest): HttpResponse<EventSheetResponse> {
        val eventSheet = eventSheetService.createEventSheet(
            name = postEventSheetRequest.name,
            description = postEventSheetRequest.description,
            eventSheetType = postEventSheetRequest.eventSheetType,
            hostTimeZoneRegion = postEventSheetRequest.timeZone,
            activeStartDateTime = postEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postEventSheetRequest.activeEndDateTime,
            manualActive = postEventSheetRequest.manualActive,
            pinCode = postEventSheetRequest.pinCode,
            eventSheetTimeSlots = postEventSheetRequest.eventSheetTimeSlots.map {
                EventSheetTimeSlot(
                    date = it.date,
                    startTime = TimeUtils.timeStringToLocalTime(it.startTime),
                    endTime = TimeUtils.timeStringToLocalTime(it.endTime)
                )
            },
            wordCount = postEventSheetRequest.wordCount ?: 3,
        )
        return HttpResponse.of(eventSheet.toResponse())
    }

    @PostMapping(path = ["specific-dates"])
    @Operation(summary = "create Specific Dates Event Sheet")
    fun postSpecificDatesEventSheet(@RequestBody postSpecificDatesEventSheetRequest: PostSpecificDatesEventSheetRequest): HttpResponse<EventSheetResponse> {
        val postEventSheetRequest = postSpecificDatesEventSheetRequest.toPostEventSheetRequest()
        return postEventSheet(postEventSheetRequest)
    }

    // MARK: - 이벤트 타입에 따른 이벤트 시트 생성
    @PostMapping(path = ["recurring-weekdays"])
    @Operation(summary = "create Recurring Weekdays Event Sheet")
    fun postRecurringWeekdaysEventSheet(@RequestBody postRecurringWeekdaysEventSheetRequest: PostRecurringWeekdaysEventSheetRequest): HttpResponse<EventSheetResponse> {
        val postEventSheetRequest = postRecurringWeekdaysEventSheetRequest.toPostEventSheetRequest()
        return postEventSheet(postEventSheetRequest)
    }

    @GetMapping(path = ["/{eventCode}"])
    @Operation(summary = "read Event Sheet")
    fun getEventSheetByEventCode(
        @PathVariable eventCode: String,
        @RequestParam region: String?,
    ): HttpResponse<EventSheetResponse> {
        val eventSheet = eventSheetService.readEventSheetByEventCode(
            eventSheetCode = eventCode,
            region = region,
        )
        return HttpResponse.of(eventSheet.toResponse())
    }
}