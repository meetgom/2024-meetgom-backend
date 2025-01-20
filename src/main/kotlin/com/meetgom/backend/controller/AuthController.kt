package com.meetgom.backend.controller

import com.meetgom.backend.service.AuthService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "")
@RestController
@RequestMapping(path = ["/v1/auth"])
class AuthController(private val authService: AuthService) {

}