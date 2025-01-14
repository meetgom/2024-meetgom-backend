package com.meetgom.backend.model.http

class HttpResponse <T> (
    val isSuccess: Boolean,
    val message: String,
    val data: T,
)