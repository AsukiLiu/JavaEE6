package org.asuki.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-05-16T22:37:50.783+0900")
@StaticMetamodel(Department.class)
public class Department_ {
	public static volatile SingularAttribute<Department, String> departmentName;
	public static volatile SingularAttribute<Department, String> buildingName;
	public static volatile SingularAttribute<Department, Integer> floor;
	public static volatile ListAttribute<Department, Employee> employees;
	public static volatile MapAttribute<Department, Object, Employee> employeeMap;
}
