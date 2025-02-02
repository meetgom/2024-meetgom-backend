package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.TimeZoneRepository
import com.meetgom.backend.domain.model.common.TimeZone
import org.springframework.stereotype.Service

@Service
class UtilService(
    private val timeZoneRepository: TimeZoneRepository
) {
    fun readTimeZoneByKeyword(keyword: String?): List<TimeZone> {
        return timeZoneRepository.findByKeyword(keyword).map { it.toDomain() }
    }
}