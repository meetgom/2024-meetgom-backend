package com.meetgom.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "event_access_codes")
data class EventAccessCodeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    val event: EventEntity,

    @Column(length = 128)
    val code: String,

    @Enumerated(EnumType.STRING)
    val role: Role
)

enum class Role {
    ADMIN,  // 이벤트를 관리할 수 있는 최고 권한자
    MODERATOR,  // 이벤트 설정을 조정할 수 있지만 전체 관리는 불가능
    PARTICIPANT,  // 투표에 참여하거나 정보를 볼 수 있음
    GUEST  // 제한된 접근, 예를 들어 투표 결과만 볼 수 있음
}