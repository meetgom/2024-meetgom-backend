package com.meetgom.backend.data.entity.common

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "event_sheet_code_word")
class EventSheetCodeWordEntity(
    @Id
    @Column(name = "word", length = 32, unique = true)
    val word: String
)