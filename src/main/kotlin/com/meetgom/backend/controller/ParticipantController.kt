package com.meetgom.backend.controller

import com.meetgom.backend.controller.http.HttpResponse
import com.meetgom.backend.controller.http.request.PostAnonymousParticipantRequest
import com.meetgom.backend.controller.http.response.ParticipantResponse
import com.meetgom.backend.domain.model.participant.ParticipantAvailableTimeSlot
import com.meetgom.backend.domain.model.participant.TempParticipantAvailableTimeSlot
import com.meetgom.backend.domain.service.ParticipantService
import com.meetgom.backend.utils.TimeUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Participant", description = "")
@RestController
@RequestMapping(path = ["/v1/participate"])
class ParticipantController(private val participantService: ParticipantService) {
    @PostMapping("/{eventSheetCode}")
    @Operation(summary = "participate event sheet")
    fun postAnonymousParticipant(
        @PathVariable eventSheetCode: String,
        @RequestBody postParticipantRequest: PostAnonymousParticipantRequest,
    ): HttpResponse<ParticipantResponse> {
        val participant = participantService.createAnonymousParticipant(
            eventSheetCode = eventSheetCode,
            userName = postParticipantRequest.userName,
            password = postParticipantRequest.password,
            region = postParticipantRequest.region,
            tempAvailableTimeSlots = postParticipantRequest.availableTimeSlots.map {
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
}