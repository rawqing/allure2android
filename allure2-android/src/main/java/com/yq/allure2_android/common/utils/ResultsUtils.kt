package com.yq.allure2_android.common.utils

import android.nfc.Tag
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.google.common.io.Resources
import com.yq.allure2_android.common.utils.PropertiesUtils.loadAllureProperties
import com.yq.allure2_android.common.utils.ResultsUtils.ALLURE_DESCRIPTIONS_PACKAGE
import com.yq.allure2_android.common.utils.ResultsUtils.ALLURE_SEPARATE_LINES_SYSPROP
import com.yq.allure2_android.common.utils.ResultsUtils.MD_5
import com.yq.allure2_android.common.utils.ResultsUtils.TAG
import com.yq.allure2_android.common.utils.ResultsUtils.cachedHost
import com.yq.allure2_android.model.annotations.*
import com.yq.allure2_android.model.*
import com.yq.allure2_android.model.Link
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Boolean.parseBoolean
import java.lang.reflect.Method
import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.text.Charsets.UTF_8

object ResultsUtils {


    val ALLURE_HOST_NAME_SYSPROP = "allure.hostName"
    val ALLURE_HOST_NAME_ENV = "ALLURE_HOST_NAME"
    val ALLURE_THREAD_NAME_SYSPROP = "allure.threadName"
    val ALLURE_THREAD_NAME_ENV = "ALLURE_THREAD_NAME"
    val ALLURE_SEPARATE_LINES_SYSPROP = "allure.description.javadoc.separateLines"

    val ISSUE_LINK_TYPE = "issue"
    val TMS_LINK_TYPE = "tms"

    val EPIC_LABEL_NAME = "epic"
    val FEATURE_LABEL_NAME = "feature"
    val STORY_LABEL_NAME = "story"
    val SEVERITY_LABEL_NAME = "severity"
    val TAG_LABEL_NAME = "tag"
    val OWNER_LABEL_NAME = "owner"
    val HOST_LABEL_NAME = "host"
    val THREAD_LABEL_NAME = "thread"

    val ALLURE_DESCRIPTIONS_PACKAGE = "allureDescriptions/"
    val MD_5 = "MD5"
    val TAG = "allure_ResultsUtils"

    var cachedHost: String? = null

    fun createEpicLabel(epic: String): Label {
        return Label().withName(EPIC_LABEL_NAME).withValue(epic)
    }

    fun createFeatureLabel(feature: String): Label {
        return Label().withName(FEATURE_LABEL_NAME).withValue(feature)
    }

    fun createStoryLabel(story: String): Label {
        return Label().withName(STORY_LABEL_NAME).withValue(story)
    }

    fun createTagLabel(tag: String): Label {
        return Label().withName(TAG_LABEL_NAME).withValue(tag)
    }

    fun createOwnerLabel(owner: String): Label {
        return Label().withName(OWNER_LABEL_NAME).withValue(owner)
    }

    fun createSeverityLabel(severity: SeverityLevel): Label {
        return createSeverityLabel(severity.value())
    }

    fun createSeverityLabel(severity: String): Label {
        return Label().withName(SEVERITY_LABEL_NAME).withValue(severity)
    }

    fun createHostLabel(): Label {
        return Label().withName(HOST_LABEL_NAME).withValue(getHostName())
    }

    fun createThreadLabel(): Label {
        return Label().withName(THREAD_LABEL_NAME).withValue(getThreadName())
    }

    fun createLabel(owner: Owner): Label {
        return createOwnerLabel(owner.value)
    }

    fun createLabel(severity: Severity): Label {
        return createSeverityLabel(severity.value)
    }

    fun createLabel(story: Story): Label {
        return createStoryLabel(story.value)
    }

    fun createLabel(feature: Feature): Label {
        return createFeatureLabel(feature.value)
    }

    fun createLabel(epic: Epic): Label {
        return createEpicLabel(epic.value)
    }

    fun createIssueLink(value: String): Link {
        return createLink(value, null, null, ISSUE_LINK_TYPE)
    }

    fun createTmsLink(value: String): Link {
        return createLink(value, null, null, TMS_LINK_TYPE)
    }

    fun createLink(link: com.yq.allure2_android.model.annotations.Link): Link {
        return createLink(link.value, link.name, link.url, link.type)
    }

    fun createLink(link: com.yq.allure2_android.model.annotations.Issue): Link {
        return createIssueLink(link.value)
    }

    fun createLink(link: com.yq.allure2_android.model.annotations.TmsLink): Link {
        return createTmsLink(link.value)
    }

    fun createLink(value: String?, name: String?, url: String?, type: String): Link {
        val resolvedName = value ?: name
        val resolvedUrl = url ?: getLinkUrl(resolvedName!!, type)
        return Link()
                .withName(resolvedName)
                .withUrl(resolvedUrl)
                .withType(type)
    }

    fun getHostName(): String {
        val fromProperty = System.getProperty(ALLURE_HOST_NAME_SYSPROP)
        val fromEnv = System.getenv(ALLURE_HOST_NAME_ENV)
        return fromProperty ?: fromEnv ?: getRealHostName()
    }

    fun getThreadName(): String {
        val fromProperty = System.getProperty(ALLURE_THREAD_NAME_SYSPROP)
        val fromEnv = System.getenv(ALLURE_THREAD_NAME_ENV)
        return fromProperty ?: fromEnv ?: getRealThreadName()
    }

    fun getStatus(throwable: Throwable): Status {
        return if (throwable is AssertionError) Status.FAILED else Status.BROKEN
    }


    fun getStatusDetails(e: Throwable): StatusDetails {
        return e.let {
            StatusDetails()
                    .withMessage(it.message ?: it.javaClass.name)
                    .withTrace(getStackTraceAsString(it))
        }
    }

    fun getLinkTypePatternPropertyName(type: String): String {
        return String.format("allure.link.%s.pattern", type)
    }

    fun generateMethodSignatureHash(methodName: String, parameterTypes: List<String>): String {
        val md = getMd5Digest()
        md.update(methodName.toByteArray(UTF_8))
        parameterTypes.map { it.toByteArray(UTF_8) }
                .forEach { md.update(it) }

        return printHexBinary(md.digest()).toLowerCase()
    }

    fun getMd5Digest(): MessageDigest {
        try {
            return MessageDigest.getInstance(MD_5)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException("Can not find hashing algorithm", e)
        }

    }

    private fun getLinkUrl(name: String?, type: String): String? {
        val properties = loadAllureProperties()
        val pattern = properties.getProperty(getLinkTypePatternPropertyName(type))
        return pattern?.apply { this.replace("\\{}".toRegex(), name ?: "") }
    }

    private fun getRealHostName(): String {
        if (cachedHost == null) {
            try {
                cachedHost = InetAddress.getLocalHost().hostName
            } catch (e: Exception) {
                Log.d(TAG, "Could not get host name $e")
                cachedHost = "default"
            }

        }
        return cachedHost!!
    }

    private fun getRealThreadName(): String {
        return String.format("%s(%s)",
                Thread.currentThread().name,
                Thread.currentThread().id)
    }

    private fun getStackTraceAsString(throwable: Throwable): String {
        val stringWriter = StringWriter()
        throwable.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun processDescription(classLoader: ClassLoader, method: Method,
                           item: ExecutableItem) {
        if (method.isAnnotationPresent(Description::class.java)) {
            if (method.getAnnotation(Description::class.java).useJavaDoc) {
                val name = method.name
                val parameterTypes = method.parameterTypes
                        .map { it.typeName }

                val signatureHash = generateMethodSignatureHash(name, parameterTypes)
                val description: String
                try {
                    val resource = classLoader.getResource(ALLURE_DESCRIPTIONS_PACKAGE + signatureHash)?.apply { IOException() }
                    description = Resources.toString(resource!!, Charset.defaultCharset())
                    if (separateLines()) {
                        item.withDescriptionHtml(description.replace("\n", "<br />"))
                    } else {
                        item.withDescriptionHtml(description)
                    }
                } catch (e: IOException) {
                    Log.w(TAG, "Unable to process description resource file for method $name", e)
                }

            } else {
                val description = method.getAnnotation(Description::class.java).value
                item.withDescription(description)
            }
        }
    }

    private fun separateLines(): Boolean {
        return parseBoolean(loadAllureProperties().getProperty(ALLURE_SEPARATE_LINES_SYSPROP))
    }

}