package org.asuki.model.jackson;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

    @Test
    public void testJsonTypeInfo() {
        String jsonText = "{\"name\":\"andy\",\"orders\":[{ \"order-id\":101 },{ \"order-id\":102 }],\"extra\":\"xxx\"}";

        Person person = JsonConverters.toObject(jsonText, PersonImpl.class);
        out.println(person);

        String jsonString = JsonConverters.toString(person);
        out.println(jsonString);

        assertThat(jsonString, is(not(jsonText)));
    }

    @Test
    public void testJsonSubTypes() {
        Dto dto = new Dto();
        dto.list.add(new SubA(10, "subA"));
        dto.list.add(new SubB(20, "subB"));

        String jsonStringBefore = JsonConverters.toString(dto);
        out.println(jsonStringBefore);

        dto = JsonConverters.toObject(jsonStringBefore, Dto.class);
        out.println(dto);

        String jsonStringAfter = JsonConverters.toString(dto);

        assertThat(jsonStringBefore, is(jsonStringAfter));
    }

}

interface Order {
    int getOrderId();
}

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
class OrderImpl implements Order {
    @JsonProperty("order-id")
    private int orderId;
}

interface Person {
    String getName();

    Collection<Order> getOrders();
}

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
class PersonImpl implements Person {
    @Getter
    @Setter
    private String name;

    private Collection<Order> orders;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, defaultImpl = OrderImpl.class)
    public Collection<Order> getOrders() {
        return this.orders;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = OrderImpl.class)
    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

}

// @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(name = "SubAClass", value = SubA.class),
        @Type(name = "SubBClass", value = SubB.class) })
abstract class AbstractClass {
    public int id;
    public String name;
}

@NoArgsConstructor
class SubA extends AbstractClass {

    public SubA(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return toStringHelper(this).omitNullValues().add("id", this.id)
                .add("name", this.name).toString();
    }
}

@NoArgsConstructor
class SubB extends AbstractClass {

    public SubB(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return toStringHelper(this).omitNullValues().add("id", this.id)
                .add("name", this.name).toString();
    }
}

@ToString
class Dto {
    public List<AbstractClass> list = new ArrayList<>();
}
