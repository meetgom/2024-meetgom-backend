package com.meetgom.backend.controller

import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.controller.http.HttpResponse
import com.meetgom.backend.controller.http.request.PostEventSheetRequest
import com.meetgom.backend.controller.http.request.PostRecurringWeekdaysEventSheetRequest
import com.meetgom.backend.controller.http.request.PostSpecificDatesEventSheetRequest
import com.meetgom.backend.controller.http.response.EventSheetResponse
import com.meetgom.backend.controller.http.response.ParticipantResponse
import com.meetgom.backend.domain.service.EventSheetService
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.utils.TimeUtils
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

    @GetMapping(path = ["/{eventSheetCode}"])
    @Operation(summary = "read Event Sheet")
    fun getEventSheetByEventSheetCode(
        @PathVariable eventSheetCode: String,
        @RequestParam region: String?,
    ): HttpResponse<EventSheetResponse> {
        val eventSheet = eventSheetService.readEventSheetByEventSheetCode(
            eventSheetCode = eventSheetCode,
            region = region,
        )
        return HttpResponse.of(eventSheet.toResponse())
    }

    @DeleteMapping(path = ["/{eventSheetCode}"])
    @Operation(summary = "delete Event Sheet")
    fun deleteEventSheetByEventSheetCode(
        @PathVariable eventSheetCode: String,
    ): HttpResponse<Unit> {
        eventSheetService.deleteEventSheetByEventSheetCode(eventSheetCode)
        return HttpResponse.of(Unit)
    }


    @GetMapping(path = ["/{eventSheetCode}/{participantId}"])
    @Operation(summary = "read participant in Event Sheet")
    fun getParticipantInEventSheet(
        @PathVariable eventSheetCode: String,
        @PathVariable participantId: Long,
        @RequestParam region: String?,
    ): HttpResponse<ParticipantResponse> {
        val (eventSheetType, participant) = eventSheetService.readParticipantInEventSheet(
            eventSheetCode = eventSheetCode,
            participantId = participantId,
            region = region,
        )
        return HttpResponse.of(participant.toResponse(hideDate = eventSheetType == EventSheetType.RECURRING_WEEKDAYS))
    }
}