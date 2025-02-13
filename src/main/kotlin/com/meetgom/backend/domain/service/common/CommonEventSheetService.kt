package com.meetgom.backend.domain.service.common

import com.meetgom.backend.data.entity.common.TimeZoneEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.repository.EventSheetRepository
import com.meetgom.backend.data.repository.TimeZoneRepository
import com.meetgom.backend.data.repository.UserRepository
import com.meetgom.backend.domain.model.event_sheet.EventSheet
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import org.springframework.stereotype.Service

interface CommonEventSheetService {
    fun findEventSheetEntityByCodeWithException(eventSheetCodeValue: String): EventSheetEntity
    fun findTimeZoneEntityByRegionWithException(region: String): TimeZoneEntity
    fun deleteEventSheetEntityByEventSheetCode(eventSheetCodeValue: String): Boolean
    fun convertEventSheetTimeZone(eventSheet: EventSheet, region: String?): EventSheet
}

@Service("commonEventSheetServiceImpl")
class CommonEventSheetServiceImpl(
    private val eventSheetRepository: EventSheetRepository,
    private val timeZoneRepository: TimeZoneRepository,
) : CommonEventSheetService {
    override fun findEventSheetEntityByCodeWithException(eventSheetCodeValue: String): EventSheetEntity {
        val eventSheetEntity = eventSheetRepository.findByEventSheetCode(eventSheetCodeValue)
            ?: throw EventSheetExceptions.EVENT_SHEET_NOT_FOUND.toException()
        if (eventSheetEntity.deletedAt != null)
            throw EventSheetExceptions.EVENT_SHEET_NOT_FOUND.toException()
        return eventSheetEntity
    }

    override fun findTimeZoneEntityByRegionWithException(region: String): TimeZoneEntity {
        return timeZoneRepository.findByRegion(region)
            ?: throw EventSheetExceptions.TIME_ZONE_NOT_FOUND.toException()
    }

    override fun convertEventSheetTimeZone(eventSheet: EventSheet, region: String?): EventSheet {
        val timeZone = if (region.isNullOrEmpty()) eventSheet.hostTimeZone else
            eventSheet.getMatchedTimeZone(region) ?: findTimeZoneEntityByRegionWithException(region = region).toDomain()
        return eventSheet.convertTimeZone(timeZone)
    }

    override fun deleteEventSheetEntityByEventSheetCode(eventSheetCodeValue: String): Boolean {
        val deleteRows = eventSheetRepository.deleteByEventSheetCode(eventSheetCodeValue) ?: 0
        if (deleteRows == 0) throw EventSheetExceptions.EVENT_SHEET_NOT_FOUND.toException()
        return true
    }
}