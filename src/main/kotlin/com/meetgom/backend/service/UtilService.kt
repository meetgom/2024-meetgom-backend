package com.meetgom.backend.service

import com.meetgom.backend.repository.TimeZoneRepository
import org.springframework.stereotype.Service

@Service
class UtilService(
    private val timeZoneRepository: TimeZoneRepository
) {
    fun readActiveTimeZones(search: String?) =
        timeZoneRepository.findActiveTimeZones(search = search).map { it.toDomain().region }
}