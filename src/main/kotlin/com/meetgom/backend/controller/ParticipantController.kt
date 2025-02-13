package com.meetgom.backend.controller

import com.meetgom.backend.controller.http.HttpResponse
import com.meetgom.backend.controller.http.request.PostAnonymousParticipantRequest
import com.meetgom.backend.controller.http.request.PostStandardParticipantRequest
import com.meetgom.backend.controller.http.request.PutParticipantRequest
import com.meetgom.backend.controller.http.response.ParticipantResponse
import com.meetgom.backend.domain.model.participant.TempParticipantAvailableTimeSlot
import com.meetgom.backend.domain.service.ParticipantService
import com.meetgom.backend.exception.exceptions.ParticipantExceptions
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.utils.TimeUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Participant", description = "")
@RestController
@RequestMapping(path = ["/v1/participate"])
class ParticipantController(private val participantService: ParticipantService) {
    @PostMapping("/anonymous/{eventSheetCode}")
    @Operation(summary = "participate event sheet for anonymous user")
    fun postAnonymousParticipant(
        @PathVariable eventSheetCode: String,
        @RequestBody postAnonymousParticipantRequest: PostAnonymousParticipantRequest,
    ): HttpResponse<ParticipantResponse> {
        if (!postAnonymousParticipantRequest.passwordMatch())
            throw ParticipantExceptions.CONFIRM_PASSWORD_NOT_MATCH.toException()
        val participant = participantService.createAnonymousParticipant(
            eventSheetCode = eventSheetCode,
            userName = postAnonymousParticipantRequest.userName,
            password = postAnonymousParticipantRequest.password,
            region = postAnonymousParticipantRequest.region,
            tempAvailableTimeSlots = postAnonymousParticipantRequest.availableTimeSlots.map {
                TempParticipantAvailableTimeSlot(
                    date = it.date,
                    dayOfWeek = it.dayOfWeek,
                    startTime = TimeUtils.timeStringToLocalTime(it.startTime),
                    endTime = TimeUtils.timeStringToLocalTime(it.endTime)
                )
            }
        )
        return HttpResponse.of(participant.toResponse())
    }

    @PostMapping("/standard/{eventSheetCode}")
    @Operation(summary = "participate event sheet for standard user (미완료 / 토큰 받아서 처리 예정)")
    fun postStandardParticipant(
        @PathVariable eventSheetCode: String,
        @RequestBody postStandardParticipantRequest: PostStandardParticipantRequest,
    ): HttpResponse<ParticipantResponse> {
        val participant = participantService.createStandardParticipant(
            eventSheetCode = eventSheetCode,
            region = postStandardParticipantRequest.region,
            tempAvailableTimeSlots = postStandardParticipantRequest.availableTimeSlots.map {
                TempParticipantAvailableTimeSlot(
                    date = it.date,
                    dayOfWeek = it.dayOfWeek,
                    startTime = TimeUtils.timeStringToLocalTime(it.startTime),
                    endTime = TimeUtils.timeStringToLocalTime(it.endTime)
                )
            }
        )
        return HttpResponse.of(participant.toResponse())
    }

    @GetMapping(path = ["/{eventSheetCode}/{participantId}"])
    @Operation(summary = "read participant in Event Sheet")
    fun getParticipantInEventSheet(
        @PathVariable eventSheetCode: String,
        @PathVariable participantId: Long,
        @RequestParam region: String?,
    ): HttpResponse<ParticipantResponse> {
        val (eventSheetType, participant) = participantService.readParticipantInEventSheet(
            eventSheetCode = eventSheetCode,
            participantId = participantId,
            region = region,
        )
        return HttpResponse.of(participant.toResponse(hideDate = eventSheetType == EventSheetType.RECURRING_WEEKDAYS))
    }

    @PutMapping(path = ["/{eventSheetCode}/{participantId}"])
    @Operation(summary = "update participant in Event Sheet")
    fun putParticipantInEventSheet(
        @PathVariable eventSheetCode: String,
        @PathVariable participantId: Long,
        @RequestBody putEventSheetRequest: PutParticipantRequest
    ): HttpResponse<ParticipantResponse> {
        val participant = participantService.updateParticipantInEventSheet(
            eventSheetCode = eventSheetCode,
            participantId = participantId,
            userName = putEventSheetRequest.userName,
            region = putEventSheetRequest.region,
            tempAvailableTimeSlots = putEventSheetRequest.availableTimeSlots?.map {
                TempParticipantAvailableTimeSlot(
                    date = it.date,
                    dayOfWeek = it.dayOfWeek,
                    startTime = TimeUtils.timeStringToLocalTime(it.startTime),
                    endTime = TimeUtils.timeStringToLocalTime(it.endTime)
                )
            }
        )
        return HttpResponse.of(participant.toResponse())
    }

    @DeleteMapping(path = ["/{eventSheetCode}/{participantId}"])
    @Operation(summary = "delete participant in Event Sheet")
    fun deleteParticipantInEventSheet(
        @PathVariable eventSheetCode: String,
        @PathVariable participantId: Long,
    ): HttpResponse<Unit> {
        participantService.deleteParticipantInEventSheet(
            eventSheetCode = eventSheetCode,
            participantId = participantId
        )
        return HttpResponse.of(Unit)
    }
}