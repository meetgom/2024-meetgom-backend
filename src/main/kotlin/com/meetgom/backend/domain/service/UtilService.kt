package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.TimeZoneRepository
import org.springframework.stereotype.Service

@Service
class UtilService(
    private val timeZoneRepository: TimeZoneRepository
) {
    fun readActiveTimeZones(search: String?) =
        timeZoneRepository.findActiveTimeZones(search = search).map { it.toDomain().region }
}