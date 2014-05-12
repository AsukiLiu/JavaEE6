package org.asuki.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "DEPARTMENT")
@NoArgsConstructor
@Getter
@Setter
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DEPARTMENT_ID", nullable = false)
    private int departmentId;

    @Column(name = "DEPARTMENT_NAME", nullable = false)
    private String departmentName;

    @Column(name = "BUILDING_NAME", nullable = false)
    private String buildingName;

    @Column(name = "FLOOR", nullable = false)
    private Integer floor;

    @OneToMany(targetEntity = Employee.class, mappedBy = "department")
    private List<Employee> employees = new ArrayList<Employee>();

    @OneToMany(targetEntity = Employee.class, mappedBy = "department")
    @MapKey(name = "employeeId")
    private Map<Integer, Employee> employeeMap = new HashMap<Integer, Employee>();

}