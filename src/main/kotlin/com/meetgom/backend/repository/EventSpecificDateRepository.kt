package com.meetgom.backend.repository

import com.meetgom.backend.entity.EventSpecificDateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventSpecificDateRepository : JpaRepository<EventSpecificDateEntity, Long> {}