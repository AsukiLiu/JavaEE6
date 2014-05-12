package org.asuki.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "JOB")
@NoArgsConstructor
@Getter
@Setter
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "JOB_ID", nullable = false)
    private int jobId;

    @Column(name = "JOB_NAME", nullable = false)
    private String jobName;

}
