package com.meetgom.backend.entity.common

import com.meetgom.backend.model.domain.common.TimeZone
import jakarta.persistence.*


@Entity(name = "time_zone")
class TimeZoneEntity(
    @Id
    @Column(name = "region", length = 128)
    val region: String,

    @Column(name = "gmt_offset_str", length = 12)
    val offset: String,

    @Column(name = "gmt_offset")
    val gmtOffset: Int,

    @Column(name = "active")
    val active: Boolean
) {
    fun toDomain(): TimeZone {
        return TimeZone(
            region = this.region,
            offset = this.offset,
            gmtOffset = this.gmtOffset,
            active = this.active
        )
    }
}