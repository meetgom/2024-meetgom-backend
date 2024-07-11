package com.meetgom.backend.repository

import com.meetgom.backend.entity.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long> {
}