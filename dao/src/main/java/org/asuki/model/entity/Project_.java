package org.asuki.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.asuki.model.jackson.JsonItem;
import org.asuki.model.jackson.Tool;

@Generated(value="Dali", date="2014-06-30T23:45:26.972+0900")
@StaticMetamodel(Project.class)
public class Project_ {
	public static volatile SingularAttribute<Project, String> projectName;
	public static volatile SingularAttribute<Project, String> platform;
	public static volatile SingularAttribute<Project, Tool> tool;
	public static volatile SingularAttribute<Project, JsonItem> jsonItem;
	public static volatile SingularAttribute<Project, byte[]> logo;
	public static volatile ListAttribute<Project, String> languages;
	public static volatile MapAttribute<Project, Integer, String> steps;
}
