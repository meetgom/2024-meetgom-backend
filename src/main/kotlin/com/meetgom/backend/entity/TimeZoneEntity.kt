package com.meetgom.backend.entity

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
)