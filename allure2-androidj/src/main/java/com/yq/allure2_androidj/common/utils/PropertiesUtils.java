package com.yq.allure2_androidj.common.utils;

import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.yq.allure2_androidj.common.resultRW.AllureConstants.TAG;

/**
 * The collection of properties utils methods.
 */
public final class PropertiesUtils {

    private static final String ALLURE_PROPERTIES_FILE = "allure.properties";

    private PropertiesUtils() {
    }

    public static Properties loadAllureProperties() {
        final Properties properties = new Properties();
        if (Objects.nonNull(ClassLoader.getSystemResource(ALLURE_PROPERTIES_FILE))) {
            try (InputStream stream = ClassLoader.getSystemResourceAsStream(ALLURE_PROPERTIES_FILE)) {
                properties.load(stream);
            } catch (IOException e) {
                Log.e(TAG,"Error while reading allure.properties file from classpath: ", e);
            }
        }
        properties.putAll(System.getProperties());
        return properties;
    }

}
