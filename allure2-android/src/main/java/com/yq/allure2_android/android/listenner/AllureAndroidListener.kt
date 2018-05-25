package ru.tinkoff.allure.android

import android.support.test.internal.runner.listener.InstrumentationRunListener
import android.support.test.uiautomator.UiDevice
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
import io.qameta.allure.model.TestResult
import io.qameta.allure.*
import io.qameta.allure.model.Label
import io.qameta.allure.model.StatusDetails
import io.qameta.allure.model.Link
import org.junit.Ignore
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.text.Charsets.UTF_8


/**
 * @author Badya on 05.06.2017.
 */
class AllureAndroidListener : InstrumentationRunListener(){
    private val lifecycle = Allure.lifecycle
    private val MD_5 = "md5"
    private val testCases = object : InheritableThreadLocal<String>() {
        override fun initialValue(): String {
            return UUID.randomUUID().toString()
        }
    }


    @Throws(Exception::class)
    override fun testStarted(description: Description) {
        val uuid = testCases.get()
        val result = createTestResult(uuid, description)
        lifecycle.scheduleTestCase(result)
        lifecycle.startTestCase(uuid)
    }

    override fun testFinished(description: Description) {
    }

    override fun testFailure(failure: Failure) {
    }

    override fun testRunStarted(description: Description) {
        grantPermissions()
        //do nothing
    }

    override fun testRunFinished(result: Result) {
        //do nothing
    }


    private fun suiteFailed(failure: Failure) {
    }

    private fun grantPermissions() {
        with(UiDevice.getInstance(instrumentation)) {
            executeShellCommand("pm grant " + instrumentation.context.packageName + " android.permission.WRITE_EXTERNAL_STORAGE")
            executeShellCommand("pm grant " + instrumentation.targetContext.packageName + " android.permission.WRITE_EXTERNAL_STORAGE")
            executeShellCommand("pm grant " + instrumentation.context.packageName + " android.permission.READ_EXTERNAL_STORAGE")
            executeShellCommand("pm grant " + instrumentation.targetContext.packageName + " android.permission.READ_EXTERNAL_STORAGE")
        }
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
        description.getAnnotation(io.qameta.allure.Description::class.java)?.value?.apply { testResult.description = this }
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

    private fun getLinks(result: Description): List<Link> {
        return listOfNotNull(
                result.getAnnotation(io.qameta.allure.Link::class.java)?.let { createLink(it) },
                result.getAnnotation(Issue::class.java)?.let { createLink(it) },
                result.getAnnotation(TmsLink::class.java)?.let { createLink(it) },
                result.testClass.getAnnotation(io.qameta.allure.Link::class.java)?.let { createLink(it) },
                result.testClass.getAnnotation(Issue::class.java)?.let { createLink(it) },
                result.testClass.getAnnotation(TmsLink::class.java)?.let { createLink(it) }
        )
    }

    private fun getLabels(result: Description): List<Label> {
        return listOfNotNull(
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
        )
    }

}