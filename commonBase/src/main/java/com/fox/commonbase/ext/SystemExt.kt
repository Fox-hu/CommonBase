package com.fox.commonbase.ext

import java.io.FileInputStream
import java.nio.charset.Charset

/**
 * 返回当前的进程名
 */
fun getCurrentProcessName(): String? {
    var inputStream: FileInputStream? = null
    try {
        val fn = "/proc/self/cmdline"
        inputStream = FileInputStream(fn)
        val buffer = ByteArray(256)
        var len = 0
        var b: Int
        while (inputStream.read().also { b = it } > 0 && len < buffer.size) {
            buffer[len++] = b.toByte()
        }
        if (len > 0) {
            return String(buffer, 0, len, Charset.forName("UTF-8"))
        }
    } catch (e: Throwable) {

    } finally {
        closeQuietly(inputStream)
    }
    return null
}