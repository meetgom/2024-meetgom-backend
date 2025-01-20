package com.meetgom.backend.security

import com.meetgom.backend.utils.extends.toHexString
import java.security.SecureRandom

class SecurityValue(
    val value: String,
    val salt: String
)

class Security private constructor() {
    companion object {
        private fun generateSalt(length: Int = 16): String {
            val salt = ByteArray(length)
            val random = SecureRandom()
            random.nextBytes(salt)
            return salt.toHexString()
        }

        private fun sha256(value: String, salt: String? = null): String {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(value.toByteArray() + (salt?.toByteArray() ?: byteArrayOf()))
            return hashBytes.toHexString()
        }

        private fun encryptSha256(vararg values: String): String {
            return values.fold("") { acc, s -> sha256(acc, s) }
        }

        fun encryptPinCode(eventCode: String, pinCode: String): SecurityValue {
            val salt = generateSalt()
            return SecurityValue(encryptSha256(eventCode, pinCode, salt), salt)
        }
    }
}