package com.fox.commonbase.ext

import android.database.Cursor
import java.io.Closeable
import java.io.IOException
import java.util.zip.ZipFile

/**
 * 大部分Close关闭流，以及实现Closeable的功能可使用此方法
 *
 * @param c Closeable对象，包括Stream等
 */
fun closeQuietly(c: Closeable?) {
    try {
        c?.close()
    } catch (ioe: IOException) {
        // ignore
    }
}

/**
 * 允许“一口气”关闭多个Closeable的方法
 *
 * @param closeables 多个Closeable对象
 */
fun closeQuietly(vararg closeables: Closeable?) {
    if (closeables == null) {
        return
    }
    for (closeable in closeables) {
        closeQuietly(closeable)
    }
}

/**
 * 解决API 15及以下的Cursor都没有实现Closeable接口，因此调用Closeable参数会出现转换异常的问题
 * java.lang.IncompatibleClassChangeError: interface not implemented,
 *
 * @param c Cursor对象
 */
fun closeQuietly(c: Cursor?) {
    try {
        c?.close()
    } catch (e: Exception) {
        // ignore
    }
}

/**
 * 解决API 18及以下的ZipFile都没有实现Closeable接口，因此调用Closeable参数会出现转换异常的问题
 * java.lang.IncompatibleClassChangeError: interface not implemented,
 *
 * @param c Cursor对象
 */
fun closeQuietly(c: ZipFile?) {
    try {
        c?.close()
    } catch (e: Exception) {
        // ignore
    }
}