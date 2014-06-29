package org.asuki.model.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "employee")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "monthly_salary", nullable = false)
    private int monthlySalary;

    @Column(name = "entrance_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date entranceDate;

    // One way
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    // Two way
    @OneToOne(mappedBy = "employee", orphanRemoval = true)
    private Phone phone;

    // One way
    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "employee_email", joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "email_id", referencedColumnName = "id"))
    private List<Email> emails;

    // One way
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // Two way
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // One way
    @ManyToMany
    @JoinTable(name = "employee_project", joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
    private List<Project> projects;

    // Two way
    @ManyToMany
    @JoinTable(name = "employee_qualification", joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "qualification_id", referencedColumnName = "id"))
    private List<Qualification> qualifications;

}