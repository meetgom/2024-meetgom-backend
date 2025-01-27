package com.meetgom.backend.controller

import com.meetgom.backend.domain.service.ParticipantService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Participant", description = "")
@RestController
@RequestMapping(path = ["/v1/participate"])
class ParticipantController(private val participantService: ParticipantService) {
//    @PostMapping("/{eventCode}")
//    @Operation(summary = "participate event sheet")
//    fun postParticipate(
//        @PathVariable eventCode: String,
//        @RequestBody postParticipantRequest: PostAnonymousParticipantRequest,
//    ): HttpResponse<ParticipantResponse> {
//

//    }
}