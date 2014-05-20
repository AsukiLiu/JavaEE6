package org.asuki.model.jackson;

import static org.asuki.model.jackson.JsonUtil.*;
import static org.asuki.model.entity.Address.builder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.asuki.model.entity.Address;
import org.testng.annotations.Test;

public class JsonUtilTest {

    private static final String TEST_STRING = "{\"zipCode\":\"zipCode\",\"prefecture\":\"prefecture\",\"city\":\"city\"}";

    private static Address createTestAddress() {

        // @formatter:off
        return builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode("zipCode")
                .build();
        // @formatter:on
    }

    @Test
    public void shouldSerializeJson() {

        Address address = createTestAddress();

        assertThat(jsonSerialize(address), is(TEST_STRING));
    }

    @Test
    public void shouldDeserializeJson() {

        Address address = jsonDeserialize(TEST_STRING, Address.class);

        assertThat(address.toString(), is(createTestAddress().toString()));
    }

}
