package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.TimeZoneEntity
import com.meetgom.backend.model.http.response.TimeZoneResponse

data class TimeZone(
    val id: Long? = null,
    val name: String,
    val active: Boolean
) {
    fun toEntity(): TimeZoneEntity {
        return TimeZoneEntity(
            id = this.id,
            name = this.name,
            active = this.active
        )
    }

    fun toResponse(): TimeZoneResponse {
        return TimeZoneResponse(
            id = this.id,
            name = this.name,
            active = this.active
        )
    }
}