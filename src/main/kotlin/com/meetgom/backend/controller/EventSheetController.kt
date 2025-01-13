package com.meetgom.backend.controller

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.http.request.PostEventSheetRequest
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.service.EventSheetService
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
            eventDateType = postEventSheetRequest.eventDateType,
            hostTimeZoneRegion = postEventSheetRequest.timeZone,
            activeStartDateTime = postEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postEventSheetRequest.activeEndDateTime,
            manualActive = postEventSheetRequest.manualActive ?: true,
            eventSheetTimeSlots = postEventSheetRequest.eventSheetTimeSlots?.map {
                EventSheetTimeSlot(
                    startDateTime = it.startDateTime,
                    endDateTime = it.endDateTime
                )
            } ?: emptyList(),
            wordCount = postEventSheetRequest.wordCount ?: 3
        )
        return eventSheet.toResponse()
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