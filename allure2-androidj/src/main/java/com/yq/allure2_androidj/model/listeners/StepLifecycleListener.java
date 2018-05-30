package com.yq.allure2_androidj.model.listeners;


import com.yq.allure2_androidj.model.StepResult;

/**
 * Notifies about Allure step lifecycle events.
 *
 * @since 2.0
 */
public interface StepLifecycleListener {

    void beforeStepStart(StepResult result) ;

    void afterStepStart(StepResult result) ;

    void beforeStepUpdate(StepResult result) ;

    void afterStepUpdate(StepResult result);

    void beforeStepStop(StepResult result) ;

    void afterStepStop(StepResult result) ;

}
