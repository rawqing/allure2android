package com.yq.allure2_android.android.runner

import android.os.Bundle
import android.support.annotation.Keep
import android.support.test.runner.AndroidJUnitRunner
import com.yq.allure2_android.android.listener.AllureAndroidListener
import com.yq.allure2_android.common.Allure
import com.yq.allure2_android.common.utils.*
import java.io.File

/**
 * @author king
 */

@Suppress("unused")
@Keep
open class AllureAndroidRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle) {
        arguments.putCharSequence("listener", arguments.getCharSequence("listener")
                ?.let {
                    "$it,${AllureAndroidListener::class.java.name}"
                }
                ?: AllureAndroidListener::class.java.name)
        super.onCreate(arguments)


//        Allure.resDir = sdDirectory(resultsName)
//
//        deleteFolderFile(Allure.resDir!!, true)
//        mkresultDir()

    }
}