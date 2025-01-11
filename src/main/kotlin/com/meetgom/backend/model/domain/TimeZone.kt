package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.TimeZoneEntity

data class TimeZone(
    val id: Long? = null,
    val region: String,
    val offset: String,
    val gmtOffset: Int,
    val active: Boolean
) {
    fun toEntity(): TimeZoneEntity {
        return TimeZoneEntity(
            id = this.id,
            region = this.region,
            offset = this.offset,
            gmtOffset = this.gmtOffset,
            active = this.active
        )
    }
}