package com.meetgom.backend.controller

import com.meetgom.backend.model.http.HttpResponse
import com.meetgom.backend.model.http.request.PostStandardUserRequest
import com.meetgom.backend.model.http.response.UserResponse
import com.meetgom.backend.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "")
@RestController
@RequestMapping(path = ["/v1/auth"])
class AuthController(private val authService: AuthService) {
    @PostMapping("/sign-up")
    @Operation(summary = "create standard user")
    fun postStandardSignUp(@RequestBody postStandardUserRequest: PostStandardUserRequest): HttpResponse<UserResponse> {
        val user = authService.signUpStandard(
            displayName = postStandardUserRequest.displayName,
            email = postStandardUserRequest.email,
            password = postStandardUserRequest.password
        )
        return HttpResponse.of(user.toResponse())
    }
}