package com.yq.allure2_androidj.common.resultRW.gson;

import java.io.File;

public interface SerializationProcessor extends Serializer, Deserializer{
}

interface Serializer {
    String serialize(Object var1);

    void serialize(File var1, Object var2);
}

interface Deserializer {
    <T> T deserialize(String var1, Class<T> var2);

    <T> T deserialize(File var1, Class<T> var2);
}