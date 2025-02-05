package com.meetgom.backend.utils

class GitUtils {
    companion object {
        private fun processGitCommand(
            vararg gitCommand: String,
            errorMessagePrefix: String = ""
        ): List<String> {
            try {
                val process = ProcessBuilder("git", *gitCommand).start()
                val gitCommandOutput: List<String> = process.inputStream.bufferedReader().readLines()
                return gitCommandOutput
            } catch (e: Exception) {
                return listOf("$errorMessagePrefix ERROR: ${e.message}")
            }
        }

        fun getGitBranch(): String {
            return processGitCommand("branch", "--show-current", errorMessagePrefix = "Git Branch").first()
        }

        fun getGitLogs(): List<String> {
            return processGitCommand("log", "--oneline", errorMessagePrefix = "Git Logs")
        }
    }


}