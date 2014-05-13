package org.asuki.model.entity;

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
@Table(name = "department")
@NoArgsConstructor
@Getter
@Setter
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "department_id", nullable = false)
    private int departmentId;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "building_name", nullable = false)
    private String buildingName;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @OneToMany(targetEntity = Employee.class, mappedBy = "department")
    private List<Employee> employees = new ArrayList<Employee>();

    @OneToMany(targetEntity = Employee.class, mappedBy = "department")
    @MapKey(name = "employeeId")
    private Map<Integer, Employee> employeeMap = new HashMap<Integer, Employee>();

}