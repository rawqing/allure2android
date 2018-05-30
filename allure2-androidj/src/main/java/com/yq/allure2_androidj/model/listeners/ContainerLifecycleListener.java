package com.yq.allure2_androidj.model.listeners;

import com.yq.allure2_androidj.model.TestResultContainer;


/**
 * Notifies about Allure test container lifecycle.
 *
 * @since 2.0
 */
public interface ContainerLifecycleListener {

    void beforeContainerStart(TestResultContainer container) ;

    void afterContainerStart(TestResultContainer container) ;

    void beforeContainerUpdate(TestResultContainer container) ;

    void afterContainerUpdate(TestResultContainer container) ;

    void beforeContainerStop(TestResultContainer container) ;

    void afterContainerStop(TestResultContainer container) ;

    void beforeContainerWrite(TestResultContainer container) ;

    void afterContainerWrite(TestResultContainer container) ;

}
