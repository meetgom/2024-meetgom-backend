package com.meetgom.backend.exception

import com.meetgom.backend.http.HttpException
import org.springframework.http.HttpStatus

class MaxEventCodesReachedException :
    HttpException(HttpStatus.UNPROCESSABLE_ENTITY, "최대 이벤트 코드 개수를 초과하였습니다.")