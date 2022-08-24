package com.fox.commonbase

import com.fox.commonbase.kotlin_demo.kotlinDSL
import org.junit.Test

/**
 * @Author fox.hu
 * @Date 2020/4/23 16:33
 */
class DSLKtTest {

    @Test
    fun kotlinDSL() {
        kotlinDSL {
            append("DSL")
            println(this)
        }
    }
}