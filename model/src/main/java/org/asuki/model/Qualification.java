package org.asuki.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "QUALIFICATION")
@NoArgsConstructor
@Getter
@Setter
public class Qualification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "QUALIFICATION_ID", nullable = false)
    private int qualificationId;

    @Column(name = "QUALIFICATION_NAME", nullable = false)
    private String qualificationName;

    @Column(name = "QUALIFICATION_TYPE")
    private String qualificationType;

    @ManyToMany(targetEntity = Employee.class, mappedBy = "qualifications")
    private List<Employee> employees;

}