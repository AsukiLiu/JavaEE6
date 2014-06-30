package org.asuki.model.entity;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.LAZY;

import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.Valid;

import org.asuki.model.BaseEntity;
import org.asuki.model.jackson.Tool;
import org.asuki.model.jackson.JsonItem;
import org.hibernate.annotations.Type;

import lombok.*;

@Entity
@Table(name = "project")
@NoArgsConstructor
@Getter
@Setter
@Access(FIELD)
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column
    private String platform;

    @Basic(fetch = LAZY)
    @Lob
    private byte[] logo;

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