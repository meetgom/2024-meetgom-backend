package com.meetgom.backend.utils

class GitUtils {
    companion object {
        fun getGitLogs(): List<String> {
            try {
                val process = ProcessBuilder("git", "log", "--oneline").start()
                val gitLogs: List<String> = process.inputStream.bufferedReader().readLines()
                return gitLogs
            } catch (e: Exception) {
                e.printStackTrace()
                throw IllegalStateException("Error retrieving git log: " + e.message)
            }
        }
    }
}