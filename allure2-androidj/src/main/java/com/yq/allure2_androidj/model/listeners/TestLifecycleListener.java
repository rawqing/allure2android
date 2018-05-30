package com.yq.allure2_androidj.model.listeners;


import com.yq.allure2_androidj.model.TestResult;

/**
 * Listener that notifies about Allure Lifecycle events.
 *
 * @since 2.0
 */
public interface TestLifecycleListener {

    void beforeTestSchedule(TestResult result) ;

    void afterTestSchedule(TestResult result) ;

    void beforeTestUpdate(TestResult result) ;

    void afterTestUpdate(TestResult result) ;

    void beforeTestStart(TestResult result) ;

    void afterTestStart(TestResult result) ;

    void beforeTestStop(TestResult result) ;

    void afterTestStop(TestResult result) ;

    void beforeTestWrite(TestResult result) ;

    void afterTestWrite(TestResult result) ;

}
