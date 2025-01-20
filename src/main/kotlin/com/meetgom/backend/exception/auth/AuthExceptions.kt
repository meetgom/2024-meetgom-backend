package com.meetgom.backend.exception.auth

import com.meetgom.backend.exception.common.MGExceptionStatus
import com.meetgom.backend.exception.common.MGExceptions

enum class AuthExceptions(
    override val status: MGExceptionStatus,
    override val message: String,
) : MGExceptions {

}