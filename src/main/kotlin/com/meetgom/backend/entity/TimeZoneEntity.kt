package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.TimeZone
import jakarta.persistence.*


@Entity(name = "time_zone")
class TimeZoneEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

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
            id = this.id,
            region = this.region,
            offset = this.offset,
            gmtOffset = this.gmtOffset,
            active = this.active
        )
    }
}