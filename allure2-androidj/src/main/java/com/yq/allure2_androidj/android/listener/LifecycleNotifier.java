package com.yq.allure2_androidj.android.listener;


import com.yq.allure2_androidj.model.FixtureResult;
import com.yq.allure2_androidj.model.StepResult;
import com.yq.allure2_androidj.model.TestResult;
import com.yq.allure2_androidj.model.TestResultContainer;
import com.yq.allure2_androidj.model.listeners.ContainerLifecycleListener;
import com.yq.allure2_androidj.model.listeners.FixtureLifecycleListener;
import com.yq.allure2_androidj.model.listeners.StepLifecycleListener;
import com.yq.allure2_androidj.model.listeners.TestLifecycleListener;

import java.util.List;

/**
 * @since 2.0
 */
@SuppressWarnings("PMD.TooManyMethods")
public class LifecycleNotifier implements ContainerLifecycleListener, FixtureLifecycleListener, StepLifecycleListener ,TestLifecycleListener{

    private final List<ContainerLifecycleListener> containerListeners;

    private final List<TestLifecycleListener> testListeners;

    private final List<FixtureLifecycleListener> fixtureListeners;

    private final List<StepLifecycleListener> stepListeners;

    public LifecycleNotifier(final List<ContainerLifecycleListener> containerListeners,
                             final List<TestLifecycleListener> testListeners,
                             final List<FixtureLifecycleListener> fixtureListeners,
                             final List<StepLifecycleListener> stepListeners) {
        this.containerListeners = containerListeners;
        this.testListeners = testListeners;
        this.fixtureListeners = fixtureListeners;
        this.stepListeners = stepListeners;
    }


    @Override
    public void beforeTestSchedule(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.beforeTestSchedule(result);
        }
    }

    @Override
    public void afterTestSchedule(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.afterTestSchedule(result);
        }
    }

    @Override
    public void beforeTestUpdate(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.beforeTestUpdate(result);
        }
    }

    @Override
    public void afterTestUpdate(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.afterTestUpdate(result);
        }
    }

    @Override
    public void beforeTestStart(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.beforeTestStart(result);
        }
    }

    @Override
    public void afterTestStart(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.afterTestStart(result);
        }
    }

    @Override
    public void beforeTestStop(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.beforeTestStop(result);
        }
    }

    @Override
    public void afterTestStop(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.afterTestStop(result);
        }
    }

    @Override
    public void beforeTestWrite(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.beforeTestWrite(result);
        }
    }

    @Override
    public void afterTestWrite(final TestResult result) {
        for (TestLifecycleListener tl : testListeners) {
            tl.afterTestWrite(result);
        }
    }

    @Override
    public void beforeContainerStart(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.beforeContainerStart(container);
        }
    }

    @Override
    public void afterContainerStart(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.afterContainerStart(container);
        }
    }

    @Override
    public void beforeContainerUpdate(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.beforeContainerUpdate(container);
        }
    }

    @Override
    public void afterContainerUpdate(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.afterContainerUpdate(container);
        }
    }

    @Override
    public void beforeContainerStop(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.beforeContainerStop(container);
        }
    }

    @Override
    public void afterContainerStop(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.afterContainerStop(container);
        }
    }

    @Override
    public void beforeContainerWrite(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.beforeContainerWrite(container);
        }
    }

    @Override
    public void afterContainerWrite(final TestResultContainer container) {
        for (ContainerLifecycleListener cl : containerListeners) {
            cl.afterContainerWrite(container);
        }
    }

    @Override
    public void beforeFixtureStart(final FixtureResult result) {
        for (FixtureLifecycleListener fl : fixtureListeners) {
            fl.beforeFixtureStart(result);
        }
    }

    @Override
    public void afterFixtureStart(final FixtureResult result) {
        for (FixtureLifecycleListener fl : fixtureListeners) {
            fl.afterFixtureStart(result);
        }
    }

    @Override
    public void beforeFixtureUpdate(final FixtureResult result) {
        for (FixtureLifecycleListener fl : fixtureListeners) {
            fl.beforeFixtureUpdate(result);
        }
    }

    @Override
    public void afterFixtureUpdate(final FixtureResult result) {
        for (FixtureLifecycleListener fl : fixtureListeners) {
            fl.afterFixtureUpdate(result);
        }
    }

    @Override
    public void beforeFixtureStop(final FixtureResult result) {
        for (FixtureLifecycleListener fl : fixtureListeners) {
            fl.beforeFixtureStop(result);
        }
    }

    @Override
    public void afterFixtureStop(final FixtureResult result) {
        for (FixtureLifecycleListener fl : fixtureListeners) {
            fl.afterFixtureStop(result);
        }
    }

    @Override
    public void beforeStepStart(final StepResult result) {
        for (StepLifecycleListener sl : stepListeners) {
            sl.beforeStepStart(result);
        }
    }

    @Override
    public void afterStepStart(final StepResult result) {
        for (StepLifecycleListener sl : stepListeners) {
            sl.afterStepStart(result);
        }
    }

    @Override
    public void beforeStepUpdate(final StepResult result) {
        for (StepLifecycleListener sl : stepListeners) {
            sl.beforeStepUpdate(result);
        }
    }

    @Override
    public void afterStepUpdate(final StepResult result) {
        for (StepLifecycleListener sl : stepListeners) {
            sl.afterStepUpdate(result);
        }
    }

    @Override
    public void beforeStepStop(final StepResult result) {
        for (StepLifecycleListener sl : stepListeners) {
            sl.beforeStepStop(result);
        }
    }

    @Override
    public void afterStepStop(final StepResult result) {
        for (StepLifecycleListener sl : stepListeners) {
            sl.afterStepStop(result);
        }
    }
}
