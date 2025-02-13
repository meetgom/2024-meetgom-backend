package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM user u WHERE u.standardUserEntity.email = :email")
    fun findByEmail(email: String): Boolean
}