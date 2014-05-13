package org.asuki.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.*;

@Entity
@Table(name = "employee")
@NoArgsConstructor
@Getter
@Setter
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "employee_id", nullable = false)
    private int employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @OneToOne(targetEntity = Address.class)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;

    @OneToOne(targetEntity = Phone.class, mappedBy = "employee")
    private Phone phone;

    @OneToMany(targetEntity = Email.class)
    @JoinTable(name = "employee_email", joinColumns = @JoinColumn(name = "holder_id", referencedColumnName = "employee_id"), inverseJoinColumns = @JoinColumn(name = "email_id", referencedColumnName = "email_id"))
    private List<Email> emails;

    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    @Column(name = "entrance_date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date entranceDate;

    @ManyToOne(targetEntity = Job.class)
    @JoinColumn(name = "job_id", referencedColumnName = "job_id", nullable = false)
    private Job job;

    @Column(name = "monthly_salary", nullable = false)
    private int monthlySalary;

    @ManyToMany(targetEntity = Project.class)
    @JoinTable(name = "employee_project", joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "employee_id"), inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "project_id"))
    private List<Project> projects;

    @ManyToMany(targetEntity = Qualification.class)
    @JoinTable(name = "employee_qualification", joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "employee_id"), inverseJoinColumns = @JoinColumn(name = "qualification_id", referencedColumnName = "qualification_id"))
    private List<Qualification> qualifications;

}