package com.meetgom.backend.temp.entity

import jakarta.persistence.*

@Entity
@Table(name = "event_available_dates")
data class EventAvailableDateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val availableDateId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    val event: EventEntity,

    val date: java.time.LocalDate
)