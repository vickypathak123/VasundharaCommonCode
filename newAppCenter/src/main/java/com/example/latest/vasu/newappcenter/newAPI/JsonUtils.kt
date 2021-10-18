package com.example.latest.vasu.newappcenter.newAPI

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * JsonUtils.kt - Helper methods for json parsing.
 * @author:  Jignesh N Patel
 * @date: 09-Feb-2021 7:02 PM
 */

fun convertStreamToString(inputStream: InputStream): String {
    val reader = BufferedReader(InputStreamReader(inputStream))
    val sb = StringBuilder()
    var line: String?
    try {
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append('\n')
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return sb.toString()
}