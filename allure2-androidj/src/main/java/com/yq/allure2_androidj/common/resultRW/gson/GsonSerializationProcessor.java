package com.yq.allure2_androidj.common.resultRW.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GsonSerializationProcessor implements SerializationProcessor {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String serialize(Object var1) {
        return gson.toJson(var1);
    }

    @Override
    public void serialize(File file, Object o) {
        try(FileWriter fw = new FileWriter(file)) {
            gson.toJson(o, o.getClass(), gson.newJsonWriter(fw));

        } catch (IOException e) {
            throw new JsonIOException("Failed to serialize to file "+file.getAbsolutePath(), e);
        }
    }

    @Override
    public <T> T deserialize(String var1, Class<T> var2) {
        return gson.fromJson(var1, var2);
    }

    @Override
    public <T> T deserialize(File file, Class<T> type) {
        try(FileReader fw = new FileReader(file)) {
            return gson.fromJson(gson.newJsonReader(fw) ,type);

        } catch (IOException e) {
            throw new JsonIOException("Failed to deserialize from file "+file.getAbsolutePath(), e);
        }
    }
}
