package com.yq.allure2_android.common

import io.qameta.allure.AllureConstants.ATTACHMENT_FILE_SUFFIX
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*
import com.yq.allure2_android.android.listenner.LifecycleNotifier
import com.yq.allure2_android.model.TestResult
import com.yq.allure2_android.common.resultRW.AllureResultsWriter
import io.qameta.allure.model.*


class AllureLifecycle(
        private val writer: AllureResultsWriter,
        private val storage: AllureStorage,
        private val notifier: LifecycleNotifier
) {
    val TAG = "allure_AllureLifecycle"

    fun startTestContainer(parentUuid: String, container: TestResultContainer) {
        updateTestContainer(parentUuid, { children.add(container.uuid) })
        startTestContainer(container)
    }

    fun startTestContainer(container: TestResultContainer) {
        notifier.beforeContainerStart(container)
        container.start = System.currentTimeMillis()
        storage.addContainer(container)
        notifier.afterContainerStart(container)
    }

    fun updateTestContainer(uuid: String, update: TestResultContainer.() -> Unit) {
        storage.getContainer(uuid)?.apply{
            notifier.beforeContainerUpdate(this)
            update()
            notifier.afterContainerUpdate(this)
        }
    }

    fun stopTestContainer(uuid: String) {
        storage.getContainer(uuid)?.apply {
            notifier.beforeContainerStop(this)
            this.stop = System.currentTimeMillis()
            notifier.afterContainerUpdate(this)
        }
    }

    fun writeTestContainer(uuid: String) {
        storage.removeContainer(uuid)?.apply{
            notifier.beforeContainerWrite(this)
            writer.write(this)
            notifier.afterContainerWrite(this)
        }
    }

    fun startPrepareFixture(parentUuid: String, uuid: String, result: FixtureResult) {
        notifier.beforeFixtureStart(result)
        updateTestContainer(parentUuid, { befores.add(result) })
        startFixture(uuid, result)
        notifier.afterFixtureStart(result)
    }

    fun startTearDownFixture(parentUuid: String, uuid: String, result: FixtureResult) {
        notifier.beforeFixtureStart(result)
        updateTestContainer(parentUuid, { afters.add(result) })
        startFixture(uuid, result)
        notifier.afterFixtureStart(result)
    }

    private fun startFixture(uuid: String, result: FixtureResult) {
        storage.addFixture(uuid, result)
        result.stage = Stage.RUNNING
        result.start = System.currentTimeMillis()
        storage.clearStepContext()
        storage.startStep(uuid)
    }

    fun updateFixture(update: FixtureResult.() -> Unit) {
        updateFixture(storage.getRootStep()!!, update)
    }

    fun updateFixture(uuid: String, update: FixtureResult.() -> Unit) {
        storage.getFixture(uuid).apply{
            notifier.beforeFixtureUpdate(this)
            update()
            notifier.afterFixtureUpdate(this)
        }
    }

    fun stopFixture(uuid: String) {
        storage.removeFixture(uuid).apply {
            notifier.beforeFixtureStop(this)
            storage.clearStepContext()
            this.stage = Stage.FINISHED
            this.stop = System.currentTimeMillis()
            notifier.afterFixtureStop(this)
        }
    }

    fun getCurrentTestCase(): String {
        return storage.getRootStep() ?: ""
    }

    fun scheduleTestCase(parentUuid: String, result: TestResult) {
        updateTestContainer(parentUuid, { children.add(result.uuid) })
        scheduleTestCase(result)
    }

    fun scheduleTestCase(result: TestResult) {
        notifier.beforeTestSchedule(result)
        result.stage = Stage.SCHEDULED
        storage.addTestResult(result)
        notifier.afterTestSchedule(result)
    }

    fun startTestCase(uuid: String) {
        storage.getTestResult(uuid)?.apply{ 
            notifier.beforeTestStart(this)
            this
                    .withStage(Stage.RUNNING)
                    .withStart(System.currentTimeMillis())
            storage.clearStepContext()
            storage.startStep(uuid)
            notifier.afterTestStart(this)
        }
    }

    fun updateTestCase(update: TestResult.()->Unit) {
        val uuid = storage.getRootStep()
        updateTestCase(uuid!!, update)
    }

    fun updateTestCase(uuid: String, update: TestResult.()->Unit) {
        storage.getTestResult(uuid)?.apply{
            notifier.beforeTestUpdate(this)
            update()
            notifier.afterTestUpdate(this)
        }
    }

    fun stopTestCase(uuid: String) {
        storage.getTestResult(uuid)?.apply{
            notifier.beforeTestStop(this)
            this
                    .withStage(Stage.FINISHED)
                    .withStop(System.currentTimeMillis())
            storage.clearStepContext()
            notifier.afterTestStop(this)
        }
    }

    fun writeTestCase(uuid: String) {
        storage.removeTestResult(uuid)?.apply{
            notifier.beforeTestWrite(this)
            writer.write(this)
            notifier.afterTestWrite(this)
        }
    }

    fun startStep(uuid: String, result: StepResult) {
        storage.getCurrentStep().apply{startStep(this, uuid, result) }
    }

    fun startStep(parentUuid: String, uuid: String, result: StepResult) {
        notifier.beforeStepStart(result)
        result.stage = Stage.RUNNING
        result.start = System.currentTimeMillis()
        storage.startStep(uuid)
        storage.addStep(parentUuid, uuid, result)
        notifier.afterStepStart(result)
    }

    fun updateStep(update: StepResult.()->Unit) {
        storage.getCurrentStep().apply{ updateStep(this, update) }
    }

    fun updateStep(uuid: String, update: StepResult.()->Unit) {
        storage.getStep(uuid).apply{
            notifier.beforeStepUpdate(this)
            update()
            notifier.afterStepUpdate(this)
        }
    }

    fun stopStep() {
        storage.getCurrentStep().let{ this.stopStep(it) }
    }

    fun stopStep(uuid: String) {
        storage.removeStep(uuid)?.apply{
            notifier.beforeStepStop(this)
            this.stage = Stage.FINISHED
            this.stop = System.currentTimeMillis()
            storage.stopStep()
            notifier.afterStepStop(this)
        }
    }

    fun addAttachment(name: String, type: String,
                      fileExtension: String, body: ByteArray) {
        addAttachment(name, type, fileExtension, ByteArrayInputStream(body))
    }

    fun addAttachment(name: String, type: String?,fileExtension: String?, stream: InputStream) {
        writeAttachment(prepareAttachment(name, type, fileExtension), stream)
    }

    fun prepareAttachment(name: String, type: String?, fileExtension: String?): String {
        val currentStep = storage.getCurrentStep()
        currentStep.let{ Log.d(TAG,"Adding attachment to item with uuid $it") }
        val source = UUID.randomUUID().toString() + ATTACHMENT_FILE_SUFFIX + fileExtension
        val attachment = Attachment()
                .withName(if (isEmpty(name)) null else name)
                .withType(if (isEmpty(type)) null else type)
                .withSource(source)
        storage.get(currentStep, WithAttachments::class.java).attachments.add(attachment)
        return attachment.source
    }

    fun writeAttachment(attachmentSource: String, stream: InputStream) {
        writer.write(attachmentSource, stream)
    }

    private fun isEmpty(s: String?): Boolean {
        return s==null || s.isEmpty()
    }


}