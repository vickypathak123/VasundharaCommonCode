package com.vasu.appcenter.rateandfeedback.library_feedback

import android.os.Process
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun extractLogToString(): String {
    val result = StringBuilder("\n\n ==== SYSTEM-LOG ===\n\n")
    val pid = Process.myPid()
    try {
        val command = String.format("logcat -d -v threadtime *:*")
        val process = Runtime.getRuntime().exec(command)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var currentLine: String?
        while (reader.readLine().also { currentLine = it } != null) {
            if (currentLine != null && currentLine!!.contains(pid.toString())) {
                result.append(currentLine)
                result.append("\n")
            }
        }

    } catch (e: IOException) {
    }

    try {
        Runtime.getRuntime().exec("logcat -c")
    } catch (e: IOException) {
    }
    return result.toString()
}