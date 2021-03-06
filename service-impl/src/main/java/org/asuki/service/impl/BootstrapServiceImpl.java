package org.asuki.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Calendar.getInstance;
import static org.asuki.model.entity.Address.builder;
import static org.asuki.model.enumeration.Gender.MALE;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asuki.dao.AddressDao;
import org.asuki.dao.BaseDao;
import org.asuki.dao.DepartmentDao;
import org.asuki.dao.EmailDao;
import org.asuki.dao.EmployeeDao;
import org.asuki.dao.JobDao;
import org.asuki.dao.PhoneDao;
import org.asuki.dao.ProjectDao;
import org.asuki.dao.QualificationDao;
import org.asuki.model.entity.Address;
import org.asuki.model.entity.Department;
import org.asuki.model.entity.Email;
import org.asuki.model.entity.Employee;
import org.asuki.model.entity.Job;
import org.asuki.model.entity.Phone;
import org.asuki.model.entity.Project;
import org.asuki.model.entity.ProjectDetail;
import org.asuki.model.entity.Qualification;
import org.asuki.model.jackson.Tool;
import org.asuki.service.BootstrapService;
import org.joda.time.DateTime;

@Stateless(name = "BootstrapService")
@Local(BootstrapService.class)
public class BootstrapServiceImpl implements BootstrapService {

    @Inject
    private AddressDao addressDao;

    @Inject
    private JobDao jobDao;

    @Inject
    private EmployeeDao employeeDao;

    @Inject
    private PhoneDao phoneDao;

    @Inject
    private EmailDao emailDao;

    @Inject
    private DepartmentDao departmentDao;

    @Inject
    private ProjectDao projectDao;

    @Inject
    private QualificationDao qualificationDao;

    @Override
    public void initializeDatabase() {
        createEntities(addressDao, createAddresses());
        createEntities(jobDao, createJobs());
        createEntities(departmentDao, createDepartments());
        createEntities(projectDao, createProjects());
        createEmployee();
    }

    private Address[] createAddresses() {

        // @formatter:off
        Address[] addresses = {
                builder()
                    .city("city1")
                    .prefecture("prefecture1")
                    .zipCode("123-1234")
                    .build(),
                builder()
                    .city("city2")
                    .prefecture("prefecture2")
                    .zipCode("123-5678")
                    .build() };
        // @formatter:on

        return addresses;
    }

    private Job[] createJobs() {
        Job[] jobs = { new Job("develop"), new Job("operation") };
        return jobs;
    }

    private Department[] createDepartments() {
        Department department = new Department();
        department.setDepartmentName("Dev");
        department.setBuildingName("X building");
        department.setFloor(5);
        Department[] departments = { department };
        return departments;
    }

    private Project[] createProjects() {
        Project project = new Project();
        project.setProjectName("Demo");
        project.setPlatform("JavaEE6");

        project.setProjectDetail(createProjectDetail());

        Project[] projects = { project };
        return projects;
    }

    private ProjectDetail createProjectDetail() {
        ProjectDetail detail = new ProjectDetail();

        Tool tool = new Tool();
        tool.setToolName("Eclipse");
        tool.setVersion("4.2");
        tool.setOpenSource(true);
        tool.setPlugins(newHashSet("plugin-A", "plugin-B", "plugin-C"));
        detail.setTool(tool);
        detail.setJsonItem(tool);

        detail.setLanguages(newArrayList("Java", "C#", "PHP"));

        Map<Integer, String> steps = newHashMap();
        steps.put(1, "Plan");
        steps.put(2, "Do");
        steps.put(3, "See");
        detail.setSteps(steps);

        return detail;
    }

    private void createEmployee() {

        Address address = new Address();
        address.setZipCode("123-0000");
        address.setCity("Tokyo");
        address.setPrefecture("Tokyo Metropolis");

        Phone phone = new Phone();
        phone.setHomePhoneNumber("03-1234-5678");
        phone.setMobilePhoneNumber("080-1234-5678");

        Email gmail = new Email();
        gmail.setEmailAddress("dev@gmail.com");
        emailDao.create(gmail);
        Email ymail = new Email();
        ymail.setEmailAddress("dev@yahoo.com");
        emailDao.create(ymail);
        List<Email> emails = newArrayList(gmail, ymail);

        Project project = projectDao.findByKey(1);
        List<Project> projects = newArrayList(project);

        Department department = departmentDao.findByKey(1);

        Job job = jobDao.findByKey(1);

        Qualification qualification = new Qualification();
        qualification.setQualificationName("D-ABC");
        qualification.setQualificationType("DEV");
        List<Qualification> qualifications = newArrayList();
        qualifications.add(qualification);
        qualificationDao.create(qualification);

        Calendar cal = getInstance();
        cal.set(2014, 6, 28);
        DateTime dateTime = new DateTime("1990-08-19");

        Employee employee = new Employee();
        employee.setGender(MALE);
        employee.setMonthlySalary(2000);
        employee.setEmployeeName("Andy");
        employee.setBirthday(new Date(dateTime.getMillis()));
        employee.setEntranceDate(new Date(cal.getTimeInMillis()));
        employee.setAddress(address);
        employee.setPhone(phone);
        employee.setEmails(emails);
        employee.setProjects(projects);
        employee.setDepartment(department);
        employee.setJob(job);
        employee.setQualifications(qualifications);

        employeeDao.create(employee);
    }

    private <E extends Serializable, K extends Serializable> void createEntities(
            BaseDao<E, K> dao, E[] entities) {
        for (E e : entities) {
            dao.create(e);
        }
    }

}
