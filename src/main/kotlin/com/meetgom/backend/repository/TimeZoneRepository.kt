package com.meetgom.backend.repository

import com.meetgom.backend.entity.TimeZoneEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TimeZoneRepository : JpaRepository<TimeZoneEntity, String> {
    @Query("SELECT t FROM time_zone t WHERE t.active = true and (lower(t.region) like lower(concat('%', :search, '%')))")
    fun findActiveTimeZones(search: String?): List<TimeZoneEntity>

    @Query("SELECT t FROM time_zone t WHERE lower(t.region) = lower(:timeZoneRegion) and t.active = true")
    fun findByRegion(@Param("timeZoneRegion") timeZoneRegion: String): TimeZoneEntity?
}