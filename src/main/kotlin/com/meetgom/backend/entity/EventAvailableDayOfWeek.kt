package com.meetgom.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "event_available_days_of_week")
data class EventAvailableDayOfWeek(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    val dayOfWeek: Byte
)