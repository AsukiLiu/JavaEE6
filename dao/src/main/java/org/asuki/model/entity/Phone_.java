package org.asuki.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-05-15T23:56:33.924+0900")
@StaticMetamodel(Phone.class)
public class Phone_ {
	public static volatile SingularAttribute<Phone, Integer> holderId;
	public static volatile SingularAttribute<Phone, Employee> employee;
	public static volatile SingularAttribute<Phone, String> homePhoneNumber;
	public static volatile SingularAttribute<Phone, String> mobilePhoneNumber;
}
