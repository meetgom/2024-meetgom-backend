package com.meetgom.backend.controller

import com.meetgom.backend.controller.http.HttpResponse
import com.meetgom.backend.controller.http.request.PostStandardUserRequest
import com.meetgom.backend.controller.http.response.UserResponse
import com.meetgom.backend.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "")
@RestController
@RequestMapping(path = ["/v1/auth"])
class AuthController(private val userService: UserService) {
    @PostMapping("/sign-up/standard")
    @Operation(summary = "create standard user")
    fun postSignUpStandard(@RequestBody postStandardUserRequest: PostStandardUserRequest): HttpResponse<UserResponse> {
        val user = userService.createStandardUser(
            userName = postStandardUserRequest.userName,
            email = postStandardUserRequest.email,
            password = postStandardUserRequest.password
        )
        return HttpResponse.of(user.toResponse())
    }
}