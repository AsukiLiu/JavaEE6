package org.asuki.common.javase;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.HashMap;

import org.testng.annotations.Test;

public class ImmutableClassTest {

    @Test
    public void shouldNotChanged() {

        HashMap<String, String> properties = new HashMap<>();
        properties.put("City", "Tokyo");

        // @formatter:off
        ImmutableClass immutableClass1 = new ImmutableClass
                .ImmutableClassBuilder(101, "Andy")
                .setCompany("ABC")
                .setProperties(properties)
                .build();
        // @formatter:on

        immutableClass1.getProperties().put("test", "test");

        assertThat(immutableClass1.getProperties().toString(),
                is("{City=Tokyo}"));
    }
}
