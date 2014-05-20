package org.asuki.model.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

    private JsonUtil() {
        // Reflectionを防ぐ
        throw new Error("インスタンス化できない");
    }

    public static String jsonSerialize(Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }

    }

    public static <T> T jsonDeserialize(String json, Class<T> clazz) {

        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            return null;
        }

    }

}
