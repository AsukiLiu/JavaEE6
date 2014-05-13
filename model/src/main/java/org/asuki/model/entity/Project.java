package org.asuki.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "project")
@NoArgsConstructor
@Getter
@Setter
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "project_id", nullable = false)
    private int projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "platform")
    private String platform;

}