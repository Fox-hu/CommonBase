package com.fox.commonbase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fox.commonbase.flow.flowTest
import com.fox.commonbase.flow.shareFlowTest
import com.fox.commonbase.flow.stateFlowTest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testFlowFun()
    }

    private fun testFlowFun() {
        flowTest()
        stateFlowTest()
//        shareFlowTest()
    }
}