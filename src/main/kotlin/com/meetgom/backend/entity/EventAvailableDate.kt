package com.meetgom.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "event_available_dates")
data class EventAvailableDate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val availableDateId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    val date: java.time.LocalDate
)