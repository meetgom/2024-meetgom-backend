package com.meetgom.backend.repository

import com.meetgom.backend.entity.TimeZoneEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TimeZoneRepository : JpaRepository<TimeZoneEntity, Long>