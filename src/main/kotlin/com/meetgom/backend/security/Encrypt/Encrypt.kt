package com.meetgom.backend.security.Encrypt

import com.meetgom.backend.utils.extends.toHexString
import java.security.SecureRandom

data class SecurityValue(
    val value: String,
    val salt: String
) {
    override fun equals(other: Any?): Boolean {
        val otherValue = other as? SecurityValue ?: return false
        if (this === otherValue) return true
        return value == otherValue.value && salt == otherValue.salt
    }

    override fun hashCode(): Int {
        return 31 * value.hashCode() + salt.hashCode()
    }
}

class Security private constructor() {
    companion object {
        fun generateSalt(length: Int = 16): String {
            val salt = ByteArray(length)
            val random = SecureRandom()
            random.nextBytes(salt)
            return salt.toHexString()
        }

        fun sha256(value: String, salt: String? = null): String {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(value.toByteArray() + (salt?.toByteArray() ?: byteArrayOf()))
            return hashBytes.toHexString()
        }

        fun encryptSha256(vararg values: String): String {
            return values.reversed().fold("") { acc, s -> sha256(acc, s) }
        }
    }
}