package org.asuki.model;

import static org.asuki.model.entity.Address.builder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.asuki.model.entity.Address;
import org.asuki.model.entity.Address.AddressBuilder;
import org.testng.annotations.Test;

public class AddressTest {

    @Test
    public void testToString() {
        // @formatter:off
        AddressBuilder builder = builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode("zipCode");
        // @formatter:on

        assertThat(
                "Address.AddressBuilder(zipCode=zipCode, prefecture=prefecture, city=city)",
                is(builder.toString()));

        Address address = builder.build();

        assertThat(
                "Address{zipCode=zipCode, prefecture=prefecture, city=city}",
                is(address.toString()));
    }
}
