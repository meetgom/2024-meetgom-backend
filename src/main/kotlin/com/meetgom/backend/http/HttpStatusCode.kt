package com.meetgom.backend.http

enum class HttpStatus(
    val statusCode: Int,
    val status: String,
    val success: Boolean = true,
) {
    // 1xx (정보 응답) -  요청을 받았으며 처리 중
    CONTINUE(100, "Continue"), // 클라이언트가 요청의 첫 부분을 보냈고, 서버가 나머지 요청을 계속 보내라고 승인한 상태.
    SWITCHING_PROTOCOLS(101, "Switching Protocols"), // 클라이언트가 프로토콜 변경 요청을 했고, 서버가 이를 승인.
    EARLY_HINTS(103, "Early Hints"), // 사전 로드 리소스를 클라이언트에 제공하기 위한 상태

    // 2xx (성공) - 요청이 성공적으로 처리되었음
    OK(200, "OK"), // 요청이 성공적으로 처리되었음
    CREATED(201, "Created"), // 요청이 성공적으로 처리되었으며, 새로운 리소스가 생성됨.
    ACCEPTED(202, "Accepted"), // 요청이 접수되었지만 아직 처리되지 않음.
    NO_CONTENT(204, "No Content"), // 요청이 성공했지만, 응답 본문이 없음.
    PARTIAL_CONTENT(206, "Partial Content"), // 부분 요청이 성공적으로 처리됨 (예: 파일 다운로드의 일부).

    // 3xx: 리다이렉션 - 요청 완료를 위해 추가 작업이 필요함
    MOVED_PERMANENTLY(301, "Moved Permanently"), // 요청된 리소스가 영구적으로 새로운 URL로 이동됨.
    FOUND(302, "Found"), // 요청된 리소스가 임시적으로 다른 URL에 있음.
    SEE_OTHER(303, "See Other"), // 요청 결과를 다른 URL에서 가져오라는 의미.
    NOT_MODIFIED(304, "Not Modified"), // 클라이언트의 캐시된 리소스가 최신임.
    TEMPORARY_REDIRECT(307, "Temporary Redirect"), // 요청된 리소스가 임시적으로 다른 URL로 리다이렉트됨.
    PERMANENT_REDIRECT(308, "Permanent Redirect"), // 요청된 리소스가 영구적으로 다른 URL로 리다이렉트됨.

    // 4xx: 클라이언트 오류 - 클라이언트의 잘못된 요청으로 인해 요청이 처리되지 않음
    BAD_REQUEST(400, "Bad Request", false), // 잘못된 요청.
    UNAUTHORIZED(401, "Unauthorized", false), // 인증이 필요하거나 유효하지 않음.
    FORBIDDEN(403, "Forbidden", false), // 서버가 요청을 이해했으나, 권한 때문에 실행되지 않음.
    NOT_FOUND(404, "Not Found", false), // 요청한 리소스를 찾을 수 없음.
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", false), // 요청 메서드가 허용되지 않음.
    CONFLICT(409, "Conflict", false), // 요청이 리소스 상태와 충돌.
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity", false), // 요청은 이해되었지만, 처리가 불가능함.

    // 5xx: 서버 오류 - 서버의 문제로 인해 요청을 처리할 수 없음
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", false), // 서버에서 오류가 발생함.
    NOT_IMPLEMENTED(501, "Not Implemented", false), // 서버가 요청을 수행할 수 있는 기능을 지원하지 않음.
    BAD_GATEWAY(502, "Bad Gateway", false), // 서버가 잘못된 게이트웨이 응답을 받음.
    SERVICE_UNAVAILABLE(503, "Service Unavailable", false), // 서버가 일시적으로 요청을 처리할 수 없음.
    GATEWAY_TIMEOUT(504, "Gateway Timeout", false); // 게이트웨이가 응답하지 않아 타임아웃 발생.

    fun toException(message: String? = null): HttpException {
        return if (this.success)
            throw IllegalArgumentException("Only for error status")
        else
            HttpException(this, message)
    }
}