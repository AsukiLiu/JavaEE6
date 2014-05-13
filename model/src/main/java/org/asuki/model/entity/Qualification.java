package org.asuki.model.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "qualification")
@NoArgsConstructor
@Getter
@Setter
public class Qualification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "qualification_id", nullable = false)
    private int qualificationId;

    @Column(name = "qualification_name", nullable = false)
    private String qualificationName;

    @Column(name = "qualification_type")
    private String qualificationType;

    @ManyToMany(targetEntity = Employee.class, mappedBy = "qualifications")
    private List<Employee> employees;

}