package com.yq.allure2_android

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.util.Log
import com.yq.allure2_android.common.Allure
import com.yq.allure2_android.common.DisplayName
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Step

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@Epic("epic001")
@Feature("feature001")
class ExampleInstrumentedTest {
    @get:Rule
    var atr: ActivityTestRule<*> =  God.getActivityTestRule("com.yq.espui.LoginActivity")
    var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun bf(){
//        starWithShell()
        Thread.sleep(3000)

    }

    fun starWithShell(){
        uiDevice.executeShellCommand("am start -n yq.com.eespuai/yq.com.eespuai.LoginActivity")
    }

    @Test
    @DisplayName("首个测试")
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.yq.allure2_android", appContext.packageName)
    }


    @Test
    @DisplayName("第一个UI测试")
    fun viewtest01(){
        Allure.addAttachment("My attachment", "My attachment content")
        Allure.addAttachment("my png" ,{
            uiDevice.takeScreenshot(it)
        })
        onView(withId(1232)).perform(click())
    }

}

