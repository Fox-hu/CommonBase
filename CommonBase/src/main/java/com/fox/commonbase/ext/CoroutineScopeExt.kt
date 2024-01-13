package com.fox.commonbase.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun getIOScope(): CoroutineScope =
    CoroutineScope(
        SupervisorJob()
                + Dispatchers.IO
                + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        })

fun getMainScope(): CoroutineScope =
    CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main.immediate
                + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        })