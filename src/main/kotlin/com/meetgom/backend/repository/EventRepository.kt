package com.meetgom.backend.repository

import com.meetgom.backend.entity.EventEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<EventEntity, Long> {
}