package com.meetgom.backend.controller

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.domain.TimeZone
import com.meetgom.backend.model.http.request.EventSheetRequest
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.service.EventSheetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*


@Tag(name = "Event", description = "")
@RestController
@RequestMapping(path = ["/v1/event-sheet"])
class EventSheetController(private val eventSheetService: EventSheetService) {
    @PostMapping(path = [""])
    @Operation(summary = "create Event Sheet")
    fun postEventSheet(@RequestBody eventSheetRequest: EventSheetRequest): EventSheetResponse {
        val eventSheet = eventSheetService.createEventSheet(
            name = eventSheetRequest.name,
            description = eventSheetRequest.description,
            eventDateType = eventSheetRequest.eventDateType,
            activeStartDate = eventSheetRequest.activeStartDate,
            activeEndDate =  eventSheetRequest.activeEndDate,
            isActive = eventSheetRequest.isActive,
            eventSheetTimeSlots = eventSheetRequest.eventSheetTimeSlots?.map {
                EventSheetTimeSlot(
                    date = it.date,
                    startTime = it.startTime,
                    endTime = it.endTime
                )
            } ?: emptyList(),
            timeZoneId = eventSheetRequest.timeZoneId,
            wordCount =eventSheetRequest.wordCount
        )
        return eventSheet.toResponse()
    }

    @GetMapping(path = ["/{eventCode}"])
    @Operation(summary = "read Event Sheet")
    fun getEventSheetByPinCode(
        @PathVariable eventCode: String,
        @RequestParam key: String?
    ): EventSheetResponse {
        val eventSheet = eventSheetService.readEventSheetByEventCode(eventCode)
        return eventSheet.toResponse()
    }
}