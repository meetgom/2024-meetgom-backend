package com.meetgom.backend.utils.utils

import com.meetgom.backend.utils.utils.exception.ProcessException
import com.meetgom.backend.utils.utils.model.ProcessOutput
import java.io.File

class ProcessUtils {
    companion object {
        fun process(vararg command: String, dir: File? = null): ProcessOutput {
            try {
                val process = if (dir != null) ProcessBuilder(*command)
                    .directory(dir)
                    .start()
                else
                    ProcessBuilder(*command).start()
                val stdout = process.inputStream.bufferedReader().readText()
                val stderr = process.errorStream.bufferedReader().readText()
                val exitCode = process.waitFor()
                if (exitCode != 0) {
                    throw ProcessException(
                        message = stderr,
                        commands = command.joinToString(" "),
                        exitCode = exitCode
                    )
                }
                return ProcessOutput.success(value = stdout)
            } catch (e: ProcessException) {
                return ProcessOutput.failure(error = e, exitCode = e.exitCode)
            }
        }
    }
}