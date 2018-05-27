package com.yq.allure2_android.common.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.uiautomator.UiDevice
import android.util.Log
import java.io.File
import java.util.*

val allureTag = "allure_"


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

fun obtainDirectory(path: String): File {
    Log.d(allureTag ,"start mkdirs ~~~~~")
    val file : File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        File(Environment.getExternalStorageDirectory(), path)
    } else {
        getInstrumentation().context.getDir(path, Context.MODE_PRIVATE)
    }
//    if (!file.exists()){
//        file.createNewFile()
//    }
    return file
}


fun grantPermissions() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()

    with(UiDevice.getInstance(instrumentation)) {
        executeShellCommand("pm grant " + instrumentation.context.packageName + " android.permission.WRITE_EXTERNAL_STORAGE")
        executeShellCommand("pm grant " + instrumentation.targetContext.packageName + " android.permission.WRITE_EXTERNAL_STORAGE")
        executeShellCommand("pm grant " + instrumentation.context.packageName + " android.permission.READ_EXTERNAL_STORAGE")
        executeShellCommand("pm grant " + instrumentation.targetContext.packageName + " android.permission.READ_EXTERNAL_STORAGE")
    }
}

