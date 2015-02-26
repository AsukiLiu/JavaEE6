package org.asuki.model.jackson;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonObject {

    private int value;

    private Map<String, String> map;

    private List<String> list;

}
