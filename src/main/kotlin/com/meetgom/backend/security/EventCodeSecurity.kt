package com.meetgom.backend.security

import com.meetgom.backend.model.domain.event_sheet.EventCode
import com.meetgom.backend.security.Encrypt.Security
import com.meetgom.backend.security.Encrypt.Security.Companion.encryptSha256

class EventCodeSecurity {
    companion object {
        private fun encryptPinCode(pinCode: String, eventCodeValue: String, saltValue: String): String {
            return encryptSha256(pinCode, eventCodeValue, saltValue)
        }

        fun generateEventCodeWithEncryptedPinCode(eventCodeValue: String, pinCode: String, salt: String? = null): EventCode {
            val saltValue = salt ?: Security.generateSalt()
            val securePinCode = encryptPinCode(pinCode, eventCodeValue, saltValue)
            return EventCode(eventCodeValue, securePinCode, saltValue)
        }

        fun checkPinCode(eventCode: EventCode, pinCode: String): Boolean {
            return encryptPinCode(pinCode, eventCode.eventCode, eventCode.salt) == eventCode.pinCode
        }
    }
}