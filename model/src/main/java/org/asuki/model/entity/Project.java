package org.asuki.model.entity;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.LAZY;

import javax.persistence.Access;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

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

    @Embedded
    private ProjectDetail projectDetail;

}