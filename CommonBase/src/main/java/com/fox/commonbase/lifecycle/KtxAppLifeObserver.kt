package com.fox.commonbase.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fox.commonbase.Ktx
import com.fox.commonbase.ext.toast

class KtxAppLifeObserver : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        Ktx.app.toast("进入前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        Ktx.app.toast("进入后台")
    }
}