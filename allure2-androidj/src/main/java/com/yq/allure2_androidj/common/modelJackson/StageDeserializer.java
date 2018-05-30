package com.yq.allure2_androidj.common.modelJackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.yq.allure2_androidj.common.feature.Stream;
import com.yq.allure2_androidj.model.Stage;

import java.io.IOException;

/**
 * @author charlie (Dmitry Baev).
 */
public class StageDeserializer extends StdDeserializer<Stage> {
    protected StageDeserializer() {
        super(Stage.class);
    }

    @Override
    public Stage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.readValueAs(String.class);
        return Stream.of(Stage.values())
                .filter(status -> status.value().equalsIgnoreCase(value))
                .findFirst();
    }
}
