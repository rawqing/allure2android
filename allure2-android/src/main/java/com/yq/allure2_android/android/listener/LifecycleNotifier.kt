package com.yq.allure2_android.android.listener

import com.yq.allure2_android.model.FixtureResult
import com.yq.allure2_android.model.StepResult
import com.yq.allure2_android.model.TestResult
import com.yq.allure2_android.model.TestResultContainer
import com.yq.allure2_android.model.listeners.ContainerLifecycleListener
import com.yq.allure2_android.model.listeners.FixtureLifecycleListener
import com.yq.allure2_android.model.listeners.StepLifecycleListener
import com.yq.allure2_android.model.listeners.TestLifecycleListener

class LifecycleNotifier(
        private val containerListeners: List<ContainerLifecycleListener>,
        private val testListeners: List<TestLifecycleListener>,
        private val fixtureListeners: List<FixtureLifecycleListener>,
        private val stepListeners: List<StepLifecycleListener>
) : ContainerLifecycleListener, FixtureLifecycleListener, StepLifecycleListener ,TestLifecycleListener{



    override fun beforeTestSchedule(result: TestResult?) {
        testListeners.forEach { it.beforeTestSchedule(result) }
    }

    override fun afterTestSchedule(result: TestResult?) {
        testListeners.forEach { it.afterTestSchedule(result) }
    }

    override fun beforeTestUpdate(result: TestResult?) {
        testListeners.forEach { it.beforeTestUpdate(result) }
    }

    override fun afterTestUpdate(result: TestResult?) {
        testListeners.forEach { it.afterTestUpdate(result) }
    }

    override fun beforeTestStart(result: TestResult?) {
        testListeners.forEach { it.beforeTestStart(result) }
    }

    override fun afterTestStart(result: TestResult?) {
        testListeners.forEach { it.afterTestStart(result) }
    }

    override fun beforeTestStop(result: TestResult?) {
        testListeners.forEach { it.beforeTestStop(result) }
    }

    override fun afterTestStop(result: TestResult?) {
        testListeners.forEach { it.afterTestStop(result) }
    }

    override fun beforeTestWrite(result: TestResult?) {
        testListeners.forEach { it.beforeTestWrite(result) }
    }

    override fun afterTestWrite(result: TestResult?) {
        testListeners.forEach { it.afterTestWrite(result) }
    }

    override fun beforeContainerStart(container: TestResultContainer?) {
        containerListeners.forEach { it.beforeContainerStart(container) }
    }

    override fun afterContainerStart(container: TestResultContainer?) {
        containerListeners.forEach { it.afterContainerStart(container) }
    }

    override fun beforeContainerUpdate(container: TestResultContainer?) {
        containerListeners.forEach { it.beforeContainerUpdate(container) }
    }

    override fun afterContainerUpdate(container: TestResultContainer?) {
        containerListeners.forEach { it.afterContainerUpdate(container) }
    }

    override fun beforeContainerStop(container: TestResultContainer?) {
        containerListeners.forEach { it.beforeContainerStop(container) }
    }

    override fun afterContainerStop(container: TestResultContainer?) {
        containerListeners.forEach { it.afterContainerStop(container) }
    }

    override fun beforeContainerWrite(container: TestResultContainer?) {
        containerListeners.forEach { it.beforeContainerWrite(container) }
    }

    override fun afterContainerWrite(container: TestResultContainer?) {
        containerListeners.forEach { it.afterContainerWrite(container) }
    }

    override fun beforeFixtureStart(result: FixtureResult?) {
        fixtureListeners.forEach { it.beforeFixtureStart(result) }
    }

    override fun afterFixtureStart(result: FixtureResult?) {
        fixtureListeners.forEach { it.afterFixtureStart(result) }
    }

    override fun beforeFixtureUpdate(result: FixtureResult?) {
        fixtureListeners.forEach { it.beforeFixtureUpdate(result) }
    }

    override fun afterFixtureUpdate(result: FixtureResult?) {
        fixtureListeners.forEach { it.afterFixtureUpdate(result) }
    }

    override fun beforeFixtureStop(result: FixtureResult?) {
        fixtureListeners.forEach { it.beforeFixtureStop(result) }
    }

    override fun afterFixtureStop(result: FixtureResult?) {
        fixtureListeners.forEach { it.afterFixtureStop(result) }
    }

    override fun beforeStepStart(result: StepResult?) {
        stepListeners.forEach { it.beforeStepStart(result) }
    }

    override fun afterStepStart(result: StepResult?) {
        stepListeners.forEach { it.afterStepStart(result) }
    }

    override fun beforeStepUpdate(result: StepResult?) {
        stepListeners.forEach { it.beforeStepUpdate(result) }
    }

    override fun afterStepUpdate(result: StepResult?) {
        stepListeners.forEach { it.afterStepUpdate(result) }
    }

    override fun beforeStepStop(result: StepResult?) {
        stepListeners.forEach { it.beforeStepStop(result) }
    }

    override fun afterStepStop(result: StepResult?) {
        stepListeners.forEach { it.afterStepStop(result) }
    }
}