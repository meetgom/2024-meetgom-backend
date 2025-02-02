package com.meetgom.backend.controller

import com.meetgom.backend.controller.http.response.TimeZoneResponse
import com.meetgom.backend.domain.service.UtilService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Util", description = "")
@RestController
@RequestMapping(path = ["/v1/util"])
class UtilController(
    private val utilService: UtilService
) {
    @GetMapping("/time-zones")
    fun getTimeZones(
        @RequestParam keyword: String?
    ): TimeZoneResponse {
        val timeZoneRegions = utilService.readTimeZoneByKeyword(keyword = keyword).map { it.region }
        return TimeZoneResponse(
            timeZones = timeZoneRegions
        )
    }
}