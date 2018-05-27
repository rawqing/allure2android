package com.yq.allure2_android.common.serialization

import java.io.File

/**
 * @author king
 */
interface SerializationProcessor : Serializer, Deserializer

interface Serializer {
    fun serialize(src: Any): String
    fun serialize(file: File, src: Any)
}

interface Deserializer {
    fun <T> deserialize(string: String, type: Class<T>): T
    fun <T> deserialize(file: File, type: Class<T>): T
}

