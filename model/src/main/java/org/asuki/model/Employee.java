package org.asuki.model;

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
@Table(name = "EMPLOYEE")
@NoArgsConstructor
@Getter
@Setter
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private int employeeId;

    @Column(name = "EMPLOYEE_NAME", nullable = false)
    private String employeeName;

    @OneToOne(targetEntity = Address.class)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    private Address address;

    @OneToOne(targetEntity = Phone.class, mappedBy = "employee")
    private Phone phone;

    @OneToMany(targetEntity = Email.class)
    @JoinTable(name = "EMPLOYEE_EMAIL", joinColumns = @JoinColumn(name = "HOLDER_ID", referencedColumnName = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "EMAIL_ID", referencedColumnName = "EMAIL_ID"))
    private List<Email> emails;

    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID")
    private Department department;

    @Column(name = "ENTRANCE_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date entranceDate;

    @ManyToOne(targetEntity = Job.class)
    @JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID", nullable = false)
    private Job job;

    @Column(name = "MONTHLY_SALARY", nullable = false)
    private int monthlySalary;

    @ManyToMany(targetEntity = Project.class)
    @JoinTable(name = "EMPLOYEE_PROJECT", joinColumns = @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "PROJECT_ID"))
    private List<Project> projects;

    @ManyToMany(targetEntity = Qualification.class)
    @JoinTable(name = "EMPLOYEE_QUALIFICATION", joinColumns = @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID"), inverseJoinColumns = @JoinColumn(name = "QUALIFICATION_ID", referencedColumnName = "QUALIFICATION_ID"))
    private List<Qualification> qualifications;

}