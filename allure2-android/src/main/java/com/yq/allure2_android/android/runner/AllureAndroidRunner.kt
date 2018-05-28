package com.yq.allure2_android.android.runner

import android.os.Bundle
import android.support.annotation.Keep
import android.support.test.runner.AndroidJUnitRunner
import com.yq.allure2_android.android.listenner.AllureAndroidListener
import com.yq.allure2_android.common.Allure
import com.yq.allure2_android.common.utils.deleteFolderFile
import com.yq.allure2_android.common.utils.getResDirPath
import com.yq.allure2_android.common.utils.mkresultDir
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

        Allure.resDir = File(getResDirPath())

        deleteFolderFile(Allure.resDir!!, true)
        mkresultDir()

    }
}