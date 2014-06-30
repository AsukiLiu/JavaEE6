package org.asuki.model.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.asuki.model.enumeration.Gender;

@Generated(value="Dali", date="2014-06-30T23:08:44.013+0900")
@StaticMetamodel(Employee.class)
public class Employee_ {
	public static volatile SingularAttribute<Employee, Gender> gender;
	public static volatile SingularAttribute<Employee, String> employeeName;
	public static volatile SingularAttribute<Employee, Date> birthday;
	public static volatile SingularAttribute<Employee, Integer> monthlySalary;
	public static volatile SingularAttribute<Employee, Date> entranceDate;
	public static volatile SingularAttribute<Employee, Address> address;
	public static volatile SingularAttribute<Employee, Phone> phone;
	public static volatile ListAttribute<Employee, Email> emails;
	public static volatile SingularAttribute<Employee, Job> job;
	public static volatile SingularAttribute<Employee, Department> department;
	public static volatile ListAttribute<Employee, Project> projects;
	public static volatile ListAttribute<Employee, Qualification> qualifications;
}
