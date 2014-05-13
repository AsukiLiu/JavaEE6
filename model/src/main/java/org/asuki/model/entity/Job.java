package org.asuki.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "job")
@NoArgsConstructor
@Getter
@Setter
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "job_id", nullable = false)
    private int jobId;

    @Column(name = "job_name", nullable = false)
    private String jobName;

}
