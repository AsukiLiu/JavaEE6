package org.asuki.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-05-17T13:58:53.282+0900")
@StaticMetamodel(Address.class)
public class Address_ {
	public static volatile SingularAttribute<Address, String> zipCode;
	public static volatile SingularAttribute<Address, String> prefecture;
	public static volatile SingularAttribute<Address, String> city;
}
