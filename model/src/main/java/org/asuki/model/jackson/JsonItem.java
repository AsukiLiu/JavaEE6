package org.asuki.model.jackson;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

// @formatter:off
@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="detail")
@JsonSubTypes({
    @JsonSubTypes.Type(value=Tool.class),
    @JsonSubTypes.Type(value=None.class)
})
// @formatter:on
public interface JsonItem extends Serializable {

}
