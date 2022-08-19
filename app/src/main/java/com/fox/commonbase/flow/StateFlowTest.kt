package com.fox.commonbase.flow

import android.util.Log
import com.fox.commonbase.ext.getMainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Result(var state: String)

val uiState = MutableStateFlow(Result("Loading"))

fun stateFlowTest() {
    getMainScope().launch {
        uiState.collect {
            Log.i(FLOW_TAG, "thread : ${Thread.currentThread().name} , item = ${it.state}")
        }
    }
    uiState.value = Result("Success")
}