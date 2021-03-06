package org.asuki.model.jackson;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;
import static org.asuki.common.exception.CommonError.CANNOT_BE_INSTANCED;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.asuki.common.exception.CommonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonConverters {

    private static Logger log = LoggerFactory.getLogger(JsonConverters.class);

    private static ObjectMapper mapper = new ObjectMapper();

    private JsonConverters() {
        // prevent Reflection
        throw new CommonError(CANNOT_BE_INSTANCED);
    }

    public static String toString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object", e);
            return nullToEmpty(null);
        }
    }

    @Nullable
    public static <T> T toObject(@Nonnull String jsonString, Class<T> clazz) {

        checkNotNull(jsonString);

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error("Failed to convert json string", e);
            return null;
        }
    }

    @Nullable
    public static <T> T toObject(@Nonnull String jsonString,
            TypeReference<T> valueTypeRef) {

        checkNotNull(jsonString);

        try {
            return mapper.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            log.error("Failed to convert json string", e);
            return null;
        }
    }

}
