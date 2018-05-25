package com.yq.allure2_android.common.utils

import io.qameta.allure.AllureResultsWriteException
import io.qameta.allure.AllureResultsWriter
import io.qameta.allure.AllureUtils.generateTestResultContainerName
import io.qameta.allure.AllureUtils.generateTestResultName
import io.qameta.allure.model.TestResult
import io.qameta.allure.model.TestResultContainer
import ru.tinkoff.allure.serialization.SerializationProcessor
import ru.tinkoff.allure.serialization.gson.GsonSerializationProcessor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class FileAndroidResultsWriter (val resultsDir: File = FileAndroidResultsWriter.getDefaultResultsDir(),
                               val serializationProcessor: SerializationProcessor = GsonSerializationProcessor) : AllureResultsWriter {
    companion object {
        @JvmStatic
        fun getDefaultResultsDir() =
                File(System.getProperty("allure.results.directory", "build/allure-results"))
    }

    init {
        resultsDir.mkdirs()
    }

    override fun write(testResult: TestResult) =
            serializationProcessor.serialize(
                    File(resultsDir, generateTestResultName(testResult.uuid)),
                    testResult)


    override fun write(testResultContainer: TestResultContainer) =
            serializationProcessor.serialize(
                    File(resultsDir, generateTestResultContainerName(testResultContainer.uuid)),
                    testResultContainer)


    override fun write(dest: String, attachment: InputStream) = write(File(resultsDir, dest), attachment)

    private fun write(dest: File, attachment: InputStream) {
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
        try {
            src.copyTo(resultsDir.resolve(dest), true)
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not move attachment from ${src.absolutePath} to ${dest.absolutePath}", e)
        }
    }

    fun move(src: File, dest: File) {
        copy(src, dest)
        src.delete()
    }

}