package com.meetgom.backend.repository

import com.meetgom.backend.entity.AccessKeyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccessKeyRepository : JpaRepository<AccessKeyEntity, Long> {}