package org.asuki.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "PROJECT")
@NoArgsConstructor
@Getter
@Setter
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PROJECT_ID", nullable = false)
    private int projectId;

    @Column(name = "PROJECT_NAME", nullable = false)
    private String projectName;

    @Column(name = "PLATFORM")
    private String platform;

}