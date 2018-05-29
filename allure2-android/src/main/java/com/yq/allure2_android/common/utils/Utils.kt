package com.yq.allure2_android.common.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.uiautomator.UiDevice
import android.util.Log
import java.io.File
import com.yq.allure2_android.common.Allure
import com.yq.allure2_android.common.Allure.resDirPath


val allureTag = "allure_"
val reportName = "allure-results" + File.separator


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

/**
 * 获取可存取的目录路径
 */
fun getResDirPath() : String{
    if (Allure.resDirPath == null || "" == Allure.resDirPath){
        Allure.resDirPath = InstrumentationRegistry.getInstrumentation().targetContext.filesDir
                .absolutePath + File.separator + reportName
    }

    return Allure.resDirPath!!
}

/**
 * 创建一个res 目录
 */
fun mkresultDir(resultsDir: File? = null) :File{
    var resDir: File? = resultsDir
    if (resultsDir == null) {
        val filesDir = getResDirPath()
        resDir = File(filesDir)
    }

    if(!resDir!!.exists()) {
        val mk = resDir.mkdirs()
        Log.i(allureTag, "mkdir : $mk")
    }
    return resDir
}

/**
 * 递归删除目录及子文件
 */
fun deleteFolderFile(file: File, deleteThisPath: Boolean) {
    try {
        if (file.isDirectory) { //目录
            val files = file.listFiles()
            for (i in files!!.indices) {
                deleteFolderFile(files[i], true)
            }
        }
        if (deleteThisPath) {
            if (!file.isDirectory) { //如果是文件，删除
                file.delete()
            } else { //目录
                if (file.listFiles()!!.isEmpty()) { //目录下没有文件或者目录，删除
                    file.delete()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 获得sd card的File
 */
fun sdDirectory(path: String = reportName): File {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        return File(Environment.getExternalStorageDirectory(), path)
    } else {
        return getInstrumentation().context.getDir(path, Context.MODE_PRIVATE)
    }
}

/**
 * sd card File的绝对路径
 */
fun sdPath(path: String = reportName): String {
    return sdDirectory(path).absolutePath
}