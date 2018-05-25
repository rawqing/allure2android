package com.yq.allure2_android.common.utils

import java.util.*

fun isNull(obj : Any?):Boolean{
    return obj == null
}

fun printHexBinary(data: ByteArray, lowerCase: Boolean = false): String {
    val hexCode = "0123456789ABCDEF"
    val r = StringBuilder(data.size * 2)
    for (b in data) {
        r.append(hexCode[b.toInt() shr 4 and 0xF])
        r.append(hexCode[b.toInt() and 0xF])
    }
    return if (lowerCase) r.toString().toLowerCase() else r.toString()
}