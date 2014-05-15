package org.asuki.model.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.asuki.model.BaseEntity;

import lombok.*;

@Entity
@Table(name = "department")
@NoArgsConstructor
@Getter
@Setter
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "building_name", nullable = false)
    private String buildingName;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @OneToMany(targetEntity = Employee.class, mappedBy = "department")
    private List<Employee> employees;

    @OneToMany(targetEntity = Employee.class, mappedBy = "department")
    @MapKey(name = "id")
    private Map<Integer, Employee> employeeMap;

}