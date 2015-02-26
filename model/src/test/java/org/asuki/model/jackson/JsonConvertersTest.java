package org.asuki.model.jackson;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;

public class JsonConvertersTest {

    private static final String TEST_JSON = "{\"value\":100,\"map\":{\"key1\":\"value1\",\"key2\":\"value2\"},\"list\":[\"item1\",\"item2\"]}";

    @Test
    public void shouldConvertList() {
        JsonObject jsonObject = JsonConverters.toObject(TEST_JSON,
                JsonObject.class);

        List<JsonObject> jsonList = newArrayList(jsonObject);

        String jsonStringBefore = JsonConverters.toString(jsonList);

        jsonList = JsonConverters.toObject(jsonStringBefore,
                new TypeReference<List<JsonObject>>() {
                });

        String jsonStringAfter = JsonConverters.toString(jsonList);

        assertThat(jsonStringBefore, is(jsonStringAfter));
    }

    @Test
    public void shouldConvertMap() {
        JsonObject jsonObject = JsonConverters.toObject(TEST_JSON,
                JsonObject.class);

        Map<String, JsonObject> jsonMap = new HashMap<>();
        jsonMap.put("key", jsonObject);

        String jsonStringBefore = JsonConverters.toString(jsonMap);

        jsonMap = JsonConverters.toObject(jsonStringBefore,
                new TypeReference<Map<String, JsonObject>>() {
                });

        String jsonStringAfter = JsonConverters.toString(jsonMap);

        assertThat(jsonStringBefore, is(jsonStringAfter));
    }

}
