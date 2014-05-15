package org.asuki.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-05-15T23:56:33.931+0900")
@StaticMetamodel(Qualification.class)
public class Qualification_ {
	public static volatile SingularAttribute<Qualification, Integer> qualificationId;
	public static volatile SingularAttribute<Qualification, String> qualificationName;
	public static volatile SingularAttribute<Qualification, String> qualificationType;
	public static volatile ListAttribute<Qualification, Employee> employees;
}
