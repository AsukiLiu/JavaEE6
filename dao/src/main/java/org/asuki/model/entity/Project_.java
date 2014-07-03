package org.asuki.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-01T00:05:05.233+0900")
@StaticMetamodel(Project.class)
public class Project_ {
	public static volatile SingularAttribute<Project, String> projectName;
	public static volatile SingularAttribute<Project, String> platform;
	public static volatile SingularAttribute<Project, byte[]> logo;
	public static volatile SingularAttribute<Project, ProjectDetail> projectDetail;
}
