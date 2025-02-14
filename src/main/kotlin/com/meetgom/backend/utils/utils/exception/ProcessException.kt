package com.meetgom.backend.utils.utils.exception

class ProcessException(
    message: String,
    val commands: String,
    val exitCode: Int
) : Exception("ProcessException: $commands - exit $exitCode \n${message ?: "An unknown error occurred"}")