package com.yq.allure2_androidj.model.listeners;

import com.yq.allure2_androidj.model.FixtureResult;


/**
 * Notifies about Allure test fixtures lifecycle events.
 *
 * @since 2.0
 */
public interface FixtureLifecycleListener {

    void beforeFixtureStart(FixtureResult result) ;

    void afterFixtureStart(FixtureResult result) ;

    void beforeFixtureUpdate(FixtureResult result) ;

    void afterFixtureUpdate(FixtureResult result) ;

    void beforeFixtureStop(FixtureResult result) ;

    void afterFixtureStop(FixtureResult result) ;

}
