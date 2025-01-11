package com.meetgom.backend.controller

import com.meetgom.backend.model.domain.EventSheetTimeSlot
import com.meetgom.backend.model.http.request.PostEventSheetRequest
import com.meetgom.backend.model.http.response.EventSheetResponse
import com.meetgom.backend.service.EventSheetService
import com.meetgom.backend.utils.TimeUtil
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
            activeStartDateTime = postEventSheetRequest.activeStartDateTime,
            activeEndDateTime = postEventSheetRequest.activeEndDateTime,
            manualActive = postEventSheetRequest.manualActive ?: true,
            eventSheetTimeSlots = postEventSheetRequest.eventSheetTimeSlots?.map {
                EventSheetTimeSlot(
                    date = it.date,
                    startTime = TimeUtil.timeStringToTimeInt(it.startTime),
                    endTime = TimeUtil.timeStringToTimeInt(it.endTime)
                )
            } ?: emptyList(),
            timeZoneRegion = postEventSheetRequest.timeZone,
            wordCount = postEventSheetRequest.wordCount ?: 3
        )
        return eventSheet.toResponse()
    }

    @GetMapping(path = ["/{eventCode}"])
    @Operation(summary = "read Event Sheet")
    fun getEventSheetByEventCode(
        @PathVariable eventCode: String,
        @RequestParam key: String?
    ): EventSheetResponse {
        val eventSheet = eventSheetService.readEventSheetByEventCode(eventCode)
        return eventSheet.toResponse()
    }
}