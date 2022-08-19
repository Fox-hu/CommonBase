package com.fox.commonbase.flow

import android.util.Log
import com.fox.commonbase.ext.getMainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val singleEvent = MutableSharedFlow<String>()

fun shareFlowTest(){
    getMainScope().launch {
        singleEvent.collect{
            Log.i(FLOW_TAG, "thread : ${Thread.currentThread().name} , item = $it")
        }
    }
    singleEvent.tryEmit("hello")
    singleEvent.tryEmit("share flow")
}