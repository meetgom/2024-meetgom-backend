package com.meetgom.backend.utils.utils.model

import com.meetgom.backend.utils.utils.exception.ProcessException

data class ProcessOutput(
    val success: Boolean,
    val output: String?,
    val error: ProcessException?,
    val exitCode: Int
) {
    companion object {
        fun success(value: String): ProcessOutput {
            return ProcessOutput(
                success = true,
                output = value,
                error = null,
                exitCode = 0
            )
        }

        fun failure(error: ProcessException, exitCode: Int): ProcessOutput {
            return ProcessOutput(
                success = false,
                output = null,
                error = error,
                exitCode = exitCode
            )
        }
    }
}