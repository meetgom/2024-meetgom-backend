package com.meetgom.backend.service

import com.meetgom.backend.repository.TimeZoneRepository
import org.springframework.stereotype.Service

@Service
class UtilService(
    private val timeZoneRepository: TimeZoneRepository
) {
    fun readActiveTimeZones() = timeZoneRepository.findActiveTimeZones().map { it.toDomain().region }
}