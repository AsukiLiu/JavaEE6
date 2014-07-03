package org.asuki.model.entity;

import static javax.persistence.FetchType.LAZY;

import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.MapKeyColumn;
import javax.validation.Valid;

import org.asuki.model.jackson.JsonItem;
import org.asuki.model.jackson.Tool;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ProjectDetail {

    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "language")
    private List<String> languages;

    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "step")
    @MapKeyColumn(name = "sequence")
    private Map<Integer, String> steps;

    @Type(type = "org.asuki.model.hibernate.ToolType")
    @Valid
    private Tool tool;

    @Type(type = "org.asuki.model.hibernate.JsonItemType")
    @Column(name = "json_item")
    @Valid
    private JsonItem jsonItem;

}
