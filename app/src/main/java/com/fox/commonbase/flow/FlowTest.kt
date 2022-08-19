package com.fox.commonbase.flow

import android.util.Log
import com.fox.commonbase.ext.getMainScope
import com.fox.commonbase.ext.logi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

val FLOW_TAG = "FLOW"

private val testFlow=  flow<String>{
    emit("hello")
    emit("flow")
}

fun flowTest() {
    getMainScope().launch {
        testFlow.collect {
            Log.i(FLOW_TAG,"thread : ${Thread.currentThread().name} , item = $it")
        }
    }
}