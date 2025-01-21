package com.meetgom.backend.security

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig(
//    private val jwtAuthFilter: JwtAuthFilter,
//    private val authenticationEntryPointHandler: AuthenticationEntryPointHandler,
//    private val accessDeniedHandler: AccessDeinedHandler
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun mvc(introspector: HandlerMappingIntrospector): MvcRequestMatcher.Builder {
        return MvcRequestMatcher.Builder(introspector)
    }

    @Bean
    fun config(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {
        val mvc: MvcRequestMatcher.Builder = MvcRequestMatcher.Builder(introspector)

        // White List 목록
        val permitAllWhiteList: List<MvcRequestMatcher> = listOf(
            mvc.pattern("/v1/**")
        )
//
        http
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(*permitAllWhiteList.toTypedArray()).permitAll()
                    .anyRequest().authenticated()
            }
//            .cors { it.configurationSource(corsConfigurationSource()) }
//            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
//            .authenticationProvider(authenticationProvider())
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.setAllowedOriginPatterns(listOf("*"))
        configuration.allowedMethods = listOf("HEAD", "POST", "GET", "DELETE", "PUT")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}