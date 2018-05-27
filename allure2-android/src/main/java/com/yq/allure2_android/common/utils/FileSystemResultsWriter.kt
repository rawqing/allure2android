package com.yq.allure2_android.common.utils

import android.support.test.InstrumentationRegistry
import android.util.Log
import com.yq.allure2_android.common.Allure
import com.yq.allure2_android.common.resultRW.AllureResultsWriter
import com.yq.allure2_android.common.serialization.SerializationProcessor
import com.yq.allure2_android.common.serialization.gson.GsonSerializationProcessor
import com.yq.allure2_android.model.TestResult
import io.qameta.allure.AllureUtils.generateTestResultContainerName
import io.qameta.allure.AllureUtils.generateTestResultName
import io.qameta.allure.model.TestResultContainer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.reflect.KProperty

class FileAndroidResultsWriter (val serializationProcessor: SerializationProcessor = GsonSerializationProcessor) : AllureResultsWriter {

    private val TAG = allureTag+"FARWriter"
    private val resultsDir: File by lazy { Allure.resDir?: mkresultDir() }



    override fun write(testResult: TestResult) {
//        resultsDir = mkresultDir(resultsDir)
        serializationProcessor.serialize(
                File(resultsDir, generateTestResultName(testResult.uuid)),
                testResult)
    }

    override fun write(testResultContainer: TestResultContainer) {
//        resultsDir = mkresultDir(resultsDir)

        serializationProcessor.serialize(
                File(resultsDir, generateTestResultContainerName(testResultContainer.uuid)),
                testResultContainer)
    }

    override fun write(dest: String, attachment: InputStream) {
//        resultsDir = mkresultDir(resultsDir)

        write(File(resultsDir, dest), attachment)
    }


    private fun write(dest: File, attachment: InputStream) {
//        resultsDir = mkresultDir(resultsDir)

        try {
            FileOutputStream(dest).use { output ->
                attachment.use { input ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not write attachment to ${dest.absolutePath}", e)
        }
    }

    fun copy(src: File, dest: File) {
//        resultsDir = mkresultDir(resultsDir)

        try {
            src.copyTo(resultsDir!!.resolve(dest), true)
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not move attachment from ${src.absolutePath} to ${dest.absolutePath}", e)
        }
    }

    fun move(src: File, dest: File) {
//        resultsDir = mkresultDir(resultsDir)

        copy(src, dest)
        src.delete()
    }

}


class AllureResultsWriteException(message: String?, cause: Throwable) : RuntimeException(message, cause)
