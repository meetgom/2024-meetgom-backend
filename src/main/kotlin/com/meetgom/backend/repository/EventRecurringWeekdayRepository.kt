package com.meetgom.backend.repository

import com.meetgom.backend.entity.EventRecurringWeekdayEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventRecurringWeekdayRepository : JpaRepository<EventRecurringWeekdayEntity, Long> {}