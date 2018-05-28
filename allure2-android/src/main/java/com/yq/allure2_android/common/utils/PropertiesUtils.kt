package com.yq.allure2_android.common.utils

import android.nfc.Tag
import android.util.Log
import java.io.IOException
import java.util.*

object PropertiesUtils {

    private val ALLURE_PROPERTIES_FILE = "allure.properties"
    private val TAG = "${allureTag}PropertiesUtils"

    fun loadAllureProperties(): Properties {
        val properties = Properties()
        ClassLoader.getSystemResource(ALLURE_PROPERTIES_FILE)?.apply {
            try {
                ClassLoader.getSystemResourceAsStream(ALLURE_PROPERTIES_FILE).use { stream -> properties.load(stream) }
            } catch (e: IOException) {
                Log.e(TAG ,"Error while reading allure.properties file from classpath: %s", e)
            }

        }
        properties.putAll(System.getProperties())
        return properties
    }
}