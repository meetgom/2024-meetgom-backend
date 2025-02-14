package com.meetgom.backend.utils.utils

import com.meetgom.backend.utils.utils.model.GitInfo
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Git
import java.io.File
import java.net.URI


class GitUtils {
    companion object {
        private fun getGitBranch(dir: File): String {
            val output = ProcessUtils.process("git", "branch", "--show-current", dir = dir)
            return if (output.success)
                output.output?.trim() ?: "Unknown"
            else
                output.error?.message ?: "Unknown Exception"
        }

        private fun getGitLogs(dir: File): List<String> {
            val output = ProcessUtils.process("git", "log", "--oneline", dir = dir)
            return if (output.success)
                output.output?.split("\n") ?: emptyList()
            else
                listOf(output.error?.message ?: "Unknown Exception")
        }

        fun gitInfo(logLimit: Int = 3): GitInfo? {
            try {
                val locationPath = Companion::class.java.protectionDomain.codeSource.location.path
                val home = System.getProperty("user.home")
                val jarPath = locationPath.substring("nested:".length, locationPath.indexOf("!"))
                var dir = File(URI.create("file://${jarPath}"))
                while (dir.path != home) {
                    if (dir.resolve(".git").exists()) break
                    dir = dir.parentFile
                }
                return GitInfo(
                    gitDir = dir.absolutePath,
                    branch = getGitBranch(dir = dir),
                    logs = getGitLogs(dir = dir).take(logLimit)
                )
            } catch (e: Exception) {
                println("e: ${e.message}")
                return null
            }
        }
    }
}