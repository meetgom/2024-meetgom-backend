package com.meetgom.backend.security

import lombok.Getter
import lombok.Setter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Configuration
@ConfigurationProperties(prefix = "meetgom.security")
@Getter
@Setter
@Validated
class SecurityConfigProperties {
    var secret: String? = null
    var accessTokenExpiry: Long? = 3600
    var refreshTokenExpiry: Long? = 86400
}