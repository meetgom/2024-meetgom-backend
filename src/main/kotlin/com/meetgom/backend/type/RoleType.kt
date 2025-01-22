package com.meetgom.backend.type

enum class RoleType {
    ADMIN,
    HOST, // 이벤트를 관리할 수 있는 해당 이벤트의 최고 권한자
    MODERATOR,  // 이벤트 설정을 조정할 수 있지만 전체 관리는 불가능
    PARTICIPANT,  // 투표에 참여하거나 정보를 볼 수 있음
    GUEST  // 제한된 접근, 예를 들어 투표 결과만 볼 수 있음
}