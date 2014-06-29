package org.asuki.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column
    private String platform;

    @Type(type = "org.asuki.model.hibernate.ToolType")
    @Valid
    private Tool tool;

    @Type(type = "org.asuki.model.hibernate.JsonItemType")
    @Column(name = "json_item")
    @Valid
    private JsonItem jsonItem;

}