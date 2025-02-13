package com.meetgom.backend.domain.model.common

import com.meetgom.backend.data.entity.common.TimeZoneEntity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone as JavaTimeZone

data class TimeZone(
    val region: String,
    val offset: String,
    val gmtOffset: Int,
    val active: Boolean
) {
    companion object {
        private val defaultJavaTimeZone: JavaTimeZone = JavaTimeZone.getDefault()
        private val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val defaultTimeZone: TimeZone = TimeZone(
            region = defaultZoneId.id,
            offset = "GMT${ZonedDateTime.now(defaultZoneId).offset}",
            gmtOffset = defaultJavaTimeZone.getOffset(System.currentTimeMillis()) / (1000 * 60),
            active = true
        )
    }

    fun toEntity(): TimeZoneEntity {
        return TimeZoneEntity(
            region = this.region,
            offset = this.offset,
            gmtOffset = this.gmtOffset,
            active = this.active
        )
    }
}