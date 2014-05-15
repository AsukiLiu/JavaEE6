package org.asuki.model.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "qualification")
@NoArgsConstructor
@Getter
@Setter
public class Qualification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "qualification_name", nullable = false)
    private String qualificationName;

    @Column(name = "qualification_type")
    private String qualificationType;

    @ManyToMany(targetEntity = Employee.class, mappedBy = "qualifications")
    private List<Employee> employees;

}