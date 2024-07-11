package com.meetgom.backend.entity

import jakarta.persistence.*
import jdk.jfr.Event

@Entity
@Table(name = "event_available_days_of_week")
data class EventAvailableDayOfWeekEntity(
    @Id
    val eventId: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "event_id")
    val event: EventEntity,

    val dayOfWeek: Byte,
)