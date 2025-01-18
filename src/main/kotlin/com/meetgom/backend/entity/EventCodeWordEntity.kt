package com.meetgom.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType


@Entity(name = "event_code_word")
class EventCodeWordEntity(
    @Id
    @Column(name = "word", length = 32, unique = true)
    val word: String
)