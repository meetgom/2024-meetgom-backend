package com.meetgom.backend.temp.entity

import jakarta.persistence.*

@Entity
@Table(name = "time_zone")
class TimeZoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(length = 30)
    val name: String = ""

    @Column
    val active: Boolean = true
}