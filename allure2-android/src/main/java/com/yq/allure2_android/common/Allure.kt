package com.yq.allure2_android.common

import com.yq.allure2_android.android.listenner.LifecycleNotifier
import com.yq.allure2_android.common.utils.FileAndroidResultsWriter
import com.yq.allure2_android.common.utils.ServiceLoaderUtils
import io.qameta.allure.AllureResultsWriter
import io.qameta.allure.listener.ContainerLifecycleListener
import io.qameta.allure.listener.FixtureLifecycleListener
import io.qameta.allure.listener.StepLifecycleListener
import io.qameta.allure.listener.TestLifecycleListener
import io.qameta.allure.model.Label
import io.qameta.allure.model.Link
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.text.Charsets.UTF_8


object Allure {
    private val TXT_EXTENSION = ".txt"
    private val TEXT_PLAIN = "text/plain"

    val lifecycle: AllureLifecycle = getAllureLifecycle(FileAndroidResultsWriter())

    private fun getAllureLifecycle(writer: AllureResultsWriter): AllureLifecycle {
        val classLoader = javaClass.classLoader
        val notifier = LifecycleNotifier(
                ServiceLoaderUtils.load(ContainerLifecycleListener::class.java, classLoader),
                ServiceLoaderUtils.load(TestLifecycleListener::class.java, classLoader),
                ServiceLoaderUtils.load(FixtureLifecycleListener::class.java, classLoader),
                ServiceLoaderUtils.load(StepLifecycleListener::class.java, classLoader)
        )
        return AllureLifecycle(writer , AllureStorage(),notifier)
    }

    fun addLabels(vararg labels: Label) {
        lifecycle.updateTestCase({ this.withLabels(*labels) })
    }

    fun addLinks(vararg links: Link) {
        lifecycle.updateTestCase({ this.withLinks(*links) })
    }

    fun addDescription(description: String) {
        lifecycle.updateTestCase({ this.withDescription(description) })
    }

    fun addDescriptionHtml(descriptionHtml: String) {
        lifecycle.updateTestCase({ this.withDescriptionHtml(descriptionHtml) })
    }

    fun addAttachment(name: String, content: String) {
        lifecycle.addAttachment(name, TEXT_PLAIN, TXT_EXTENSION, content.toByteArray(UTF_8))
    }

    fun addAttachment(name: String, type: String, content: String) {
        lifecycle.addAttachment(name, type, TXT_EXTENSION, content.toByteArray(UTF_8))
    }

    fun addAttachment(name: String, type: String,
                      content: String, fileExtension: String) {
        lifecycle.addAttachment(name, type, fileExtension, content.toByteArray(UTF_8))
    }

    fun addAttachment(name: String, content: InputStream) {
        lifecycle.addAttachment(name, null, null, content)
    }

    fun addAttachment(name: String, type: String,
                      content: InputStream, fileExtension: String) {
        lifecycle.addAttachment(name, type, fileExtension, content)
    }

    fun addByteAttachmentAsync(
            name: String, type: String, body: ByteArray) {
        addByteAttachmentAsync(name, type, "", body)
    }

    fun addByteAttachmentAsync(
            name: String, type: String, fileExtension: String, body: ByteArray) {
        val source = lifecycle.prepareAttachment(name, type, fileExtension)
        launch(CommonPool){
            lifecycle.writeAttachment(source, ByteArrayInputStream(body))
        }
    }

    fun addStreamAttachmentAsync(
            name: String, type: String, body: InputStream){
        return addStreamAttachmentAsync(name, type, "", body)
    }

    fun addStreamAttachmentAsync(
            name: String, type: String, fileExtension: String, body: InputStream){
        val source = lifecycle.prepareAttachment(name, type, fileExtension)
        launch(CommonPool){
            lifecycle.writeAttachment(source, body)
        }
    }

}