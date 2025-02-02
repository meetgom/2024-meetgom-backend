package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.common.TimeZoneEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TimeZoneRepository : JpaRepository<TimeZoneEntity, String> {
    @Query("SELECT t FROM time_zone t WHERE t.active = true and (lower(t.region) like lower(concat('%', :keyword, '%')))")
    fun findByKeyword(keyword: String?): List<TimeZoneEntity>

    @Query("SELECT t FROM time_zone t WHERE lower(t.region) = lower(:timeZoneRegion) and t.active = true")
    fun findByRegion(@Param("timeZoneRegion") region: String): TimeZoneEntity?
}