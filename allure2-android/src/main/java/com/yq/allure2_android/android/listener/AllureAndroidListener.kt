package com.yq.allure2_android.android.listener

import android.support.test.internal.runner.listener.InstrumentationRunListener
import com.yq.allure2_android.common.Allure
import org.junit.runner.Description
import org.junit.runner.Result
import org.junit.runner.notification.Failure
import java.util.*
import com.yq.allure2_android.common.DisplayName
import com.yq.allure2_android.common.utils.ResultsUtils.createLabel
import com.yq.allure2_android.common.utils.ResultsUtils.createLink
import com.yq.allure2_android.common.utils.ResultsUtils.getHostName
import com.yq.allure2_android.common.utils.ResultsUtils.getThreadName
import com.yq.allure2_android.model.annotations.*
import org.junit.Ignore
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.text.Charsets.UTF_8
import com.yq.allure2_android.common.utils.ResultsUtils.getStatusDetails
import com.yq.allure2_android.common.utils.ResultsUtils.getStatus
import com.yq.allure2_android.common.utils.grantPermissions
import com.yq.allure2_android.model.*
import com.yq.allure2_android.model.Link


/**
 * @author king
 */
open class AllureAndroidListener : InstrumentationRunListener(){
    private val lifecycle = Allure.lifecycle
    private val MD_5 = "md5"
    private var testCases = object : InheritableThreadLocal<String>() {
        override fun initialValue(): String {
            return UUID.randomUUID().toString()
        }
    }

    private var testContainer = object : InheritableThreadLocal<String>() {
        override fun initialValue(): String {
            return UUID.randomUUID().toString()
        }
    }


    @Throws(Exception::class)
    override fun testStarted(description: Description) {
        val uuid = testCases.get()
        val result = createTestResult(uuid, description)
        lifecycle.scheduleTestCase(testContainer.get(),result)
        lifecycle.startTestCase(uuid)
    }

    override fun testFinished(description: Description) {
        val uuid = testCases.get()
        testCases.remove()
        lifecycle.updateTestCase { this.status = this.status?: Status.PASSED }

        lifecycle.stopTestCase(uuid)
        lifecycle.writeTestCase(uuid)
    }

    override fun testFailure(failure: Failure) {
        val uuid = testCases.get()
        lifecycle.updateTestCase(uuid, {
            this
                    .withStatus(getStatus(failure.exception))
                    .withStatusDetails(getStatusDetails(failure.exception))
            }
        )
    }

    override fun testAssumptionFailure(failure: Failure?) {
        val uuid = testCases.get()
        lifecycle.updateTestCase(uuid, {
            this.withStatus(Status.SKIPPED)
                    .withStatusDetails(getStatusDetails(failure!!.exception))
            }
        )
    }

    @Throws(Exception::class)
    override fun testIgnored(description: Description) {
        val uuid = testCases.get()
        testCases.remove()

        val result = createTestResult(uuid, description)
        result.status = Status.SKIPPED
        result.statusDetails = getIgnoredMessage(description)
        result.start = System.currentTimeMillis()

        lifecycle.scheduleTestCase(result)
        lifecycle.stopTestCase(uuid)
        lifecycle.writeTestCase(uuid)
    }

    override fun testRunStarted(description: Description) {
        grantPermissions()
        Allure.getResultFile()

        val uuid = testContainer.get()
        var container = TestResultContainer()
                .withUuid(uuid)
        lifecycle.startTestContainer(container)
    }

    override fun testRunFinished(result: Result) {
        val uuid = testContainer.get()
        testContainer.remove()

        lifecycle.stopTestContainer(uuid)
        lifecycle.writeTestContainer(uuid)
    }


    private fun suiteFailed(failure: Failure) {
    }



    private fun createTestResult(uuid: String, description: Description): TestResult {
        val className = description.className
        val methodName = description.methodName
        val name = methodName?:className
        val fullName = if(methodName!=null) String.format("%s.%s", className, methodName) else className

        val suite = description.testClass?.getAnnotation(DisplayName::class.java)?.value?:className

        val testResult = TestResult()
                .withUuid(uuid)
                .withHistoryId(getHistoryId(description))
                .withName(name)
                .withFullName(fullName)
                .withLinks(getLinks(description))
                .withLabels(
                        Label().withName("package").withValue(getPackage(description.testClass)),
                        Label().withName("testClass").withValue(className),
                        Label().withName("testMethod").withValue(name),
                        Label().withName("suite").withValue(suite),
                        Label().withName("host").withValue(getHostName()),
                        Label().withName("thread").withValue(getThreadName())
                )
        testResult.labels.addAll(getLabels(description))
        description.getAnnotation(DisplayName::class.java)?.value?.apply { testResult.name = this }
        description.getAnnotation(com.yq.allure2_android.model.annotations.Description::class.java)?.value?.apply { testResult.description = this }
        return testResult
    }

    private fun getHistoryId(description: Description): String {
        return md5(description.className + description.methodName)
    }

    private fun md5(source: String): String {
        val bytes = getMessageDigest().digest(source.toByteArray(UTF_8))
        return BigInteger(1, bytes).toString(16)
    }

    private fun getMessageDigest(): MessageDigest {
        try {
            return MessageDigest.getInstance(MD_5)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException("Could not find md5 hashing algorithm", e)
        }

    }

    private fun getPackage(testClass: Class<*>): String {
        return testClass.`package`?.name?:""
    }

    private fun getIgnoredMessage(description: Description): StatusDetails {
        val ignore = description.getAnnotation(Ignore::class.java)
        val message = if (ignore?.value!!.isEmpty())
            ignore.value
        else
            "Test ignored (without reason)!"
        return StatusDetails().withMessage(message)
    }

    private fun getLinks(result: Description): MutableList<Link> {
        return mutableListOf(
                result.getAnnotation(com.yq.allure2_android.model.annotations.Link::class.java)?.let { createLink(it) },
                result.getAnnotation(Issue::class.java)?.let { createLink(it) },
                result.getAnnotation(TmsLink::class.java)?.let { createLink(it) },
                result.testClass.getAnnotation(com.yq.allure2_android.model.annotations.Link::class.java)?.let { createLink(it) },
                result.testClass.getAnnotation(Issue::class.java)?.let { createLink(it) },
                result.testClass.getAnnotation(TmsLink::class.java)?.let { createLink(it) }
        ).filterNotNull().toMutableList()
    }

    private fun getLabels(result: Description): MutableList<Label>{
        return mutableListOf(
                result.getAnnotation(Epic::class.java)?.let { createLabel(it) },
                result.getAnnotation(Feature::class.java)?.let { createLabel(it) },
                result.getAnnotation(Story::class.java)?.let { createLabel(it) },
                result.getAnnotation(Severity::class.java)?.let { createLabel(it) },
                result.getAnnotation(Owner::class.java)?.let { createLabel(it) },
                result.testClass.getAnnotation(Epic::class.java)?.let { createLabel(it) },
                result.testClass.getAnnotation(Feature::class.java)?.let { createLabel(it) },
                result.testClass.getAnnotation(Story::class.java)?.let { createLabel(it) },
                result.testClass.getAnnotation(Severity::class.java)?.let { createLabel(it) },
                result.testClass.getAnnotation(Owner::class.java)?.let { createLabel(it) }
        ).filterNotNull().toMutableList()
    }

}