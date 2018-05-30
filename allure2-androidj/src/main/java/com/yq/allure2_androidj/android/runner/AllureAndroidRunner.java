package com.yq.allure2_androidj.android.runner;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import com.yq.allure2_androidj.android.listener.AllureAndroidListener;
import com.yq.allure2_androidj.common.Allure;

import java.io.File;


public class AllureAndroidRunner extends AndroidJUnitRunner {

    public void onCreate(Bundle arguments) {
        String listener = "listener";
        CharSequence sequence = arguments.getCharSequence("listener");
        if (sequence != null) {
            sequence = "" + sequence + ',' + AllureAndroidListener.class.getName();
        }else
            sequence = AllureAndroidListener.class.getName();
        arguments.putCharSequence(listener, sequence);

        super.onCreate(arguments);
    }
}
