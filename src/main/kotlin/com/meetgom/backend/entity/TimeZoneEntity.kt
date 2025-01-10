package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.TimeZone
import jakarta.persistence.*


@Entity(name = "time_zone")
class TimeZoneEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "name", length = 128)
    val name: String,

    @Column(name = "active")
    val active: Boolean
) {
    fun toDomain(): TimeZone {
        return TimeZone(
            id = this.id,
            name = this.name,
            active = this.active
        )
    }
}