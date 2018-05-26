package com.yq.allure2_android.common.utils

import android.util.Log
import java.util.*

object ServiceLoaderUtils {

    private val TAG = "${allureTag}ServiceLU"

    /**
     * Load implementation by given type.
     *
     * @param <T>         type of implementation.
     * @param type        the type of implementation to load.
     * @param classLoader the class loader to search for implementations.
     * @return loaded implementations.
    </T> */
    fun <T> load(type: Class<T>, classLoader: ClassLoader): List<T> {
        val loaded = ArrayList<T>()
        val iterator = ServiceLoader.load(type, classLoader).iterator()
        while (hasNextSafely(iterator)) {
            try {
                val next = iterator.next()
                loaded.add(next)
                Log.d(TAG,"Found $type")
            } catch (e: Exception) {
                Log.e(TAG,"Could not load $type", e)
            }

        }
        return loaded
    }

    /**
     * Safely check for <pre>iterator.hasNext()</pre>.
     *
     * @param iterator specified iterator to check he presence of next element
     * @return `true` if the iteration has more elements, false otherwise
     */
    private fun hasNextSafely(iterator: Iterator<*>): Boolean {
        try {
            /* Throw a ServiceConfigurationError if a provider-configuration file violates the specified format,
            or if it names a provider class that cannot be found and instantiated, or if the result of
            instantiating the class is not assignable to the service type, or if any other kind of exception
            or error is thrown as the next provider is located and instantiated.
            @see http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html#iterator()
            */
            return iterator.hasNext()
        } catch (e: Exception) {
            Log.e(TAG,"iterator.hasNext() failed", e)
            return false
        }

    }
}