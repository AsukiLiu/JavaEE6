package org.asuki.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

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

    @Column(name = "platform")
    private String platform;

}