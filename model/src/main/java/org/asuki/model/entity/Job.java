package org.asuki.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "job")
@NoArgsConstructor
@Getter
@Setter
public class Job extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "job_name", nullable = false)
    private String jobName;

}
