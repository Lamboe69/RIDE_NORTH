package com.ridenorth.module.auth

import com.ridenorth.common.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/request-otp")
    fun requestOtp(@Valid @RequestBody request: PhoneOtpRequest): ResponseEntity<Map<String, String>> {
        val message = authService.requestOtp(request)
        return ResponseEntity.ok(mapOf("message" to message))
    }

    @PostMapping("/verify-otp")
    fun verifyOtp(@Valid @RequestBody request: VerifyOtpRequest): ResponseEntity<AuthResponse> {
        val response = authService.verifyOtpAndAuthenticate(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register-driver")
    fun registerDriver(
        @Valid @RequestBody request: RegisterDriverRequest
    ): ResponseEntity<AuthResponse> {
        val response = authService.registerDriver(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    fun getCurrentUser(authentication: org.springframework.security.core.Authentication): ResponseEntity<UserDto> {
        val user = authService.getCurrentUser(authentication.name)
        return ResponseEntity.ok(authService.toUserDto(user))
    }
}
