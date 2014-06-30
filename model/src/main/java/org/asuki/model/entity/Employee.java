package org.asuki.model.entity;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.asuki.model.BaseEntity;
import org.asuki.model.enumeration.Gender;
import org.asuki.model.listener.CustomListener;
import org.hibernate.validator.constraints.NotBlank;

import lombok.*;

@ExcludeSuperclassListeners()
@EntityListeners({ CustomListener.class })
@Entity
@Table(name = "employee")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "employee_name", nullable = false)
    @NotBlank
    private String employeeName;

    @Temporal(DATE)
    private Date birthday;

    @Transient
    private Integer age;

    @Column(name = "monthly_salary", nullable = false)
    private int monthlySalary;

    @Column(name = "entrance_date", nullable = false)
    @Temporal(DATE)
    private Date entranceDate;

    // One way
    @OneToOne(cascade = { PERSIST, REMOVE })
    @JoinColumn(name = "address_id")
    private Address address;

    // Two way
    @OneToOne(mappedBy = "employee", cascade = ALL, orphanRemoval = true)
    private Phone phone;

    // One way
    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "employee_email", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "email_id"))
    private List<Email> emails;

    // One way
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // Two way
    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    // One way
    @ManyToMany
    @JoinTable(name = "employee_project", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;

    // Two way
    @ManyToMany
    @JoinTable(name = "employee_qualification", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "qualification_id"))
    private List<Qualification> qualifications;

}