package com.ridenorth.security

import com.ridenorth.module.user.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        val username = jwtTokenProvider.getUsernameFromToken(token)

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            userRepository.findByPhoneNumber(username).ifPresent { user ->
                if (jwtTokenProvider.validateToken(token, org.springframework.security.core.userdetails.User
                        .withUsername(user.phoneNumber)
                        .password("")
                        .authorities(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_${user.role.name}"))
                        .accountExpired(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .disabled(!user.isActive)
                        .build()
                )) {
                    val auth = UsernamePasswordAuthenticationToken(
                        user.phoneNumber,
                        null,
                        listOf(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_${user.role.name}"))
                    )
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
