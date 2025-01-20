package com.meetgom.backend.exception

import com.meetgom.backend.http.HttpException
import org.springframework.http.HttpStatus

class MaxEventCodesReachedException :
    HttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Reached the maximum number of event codes")