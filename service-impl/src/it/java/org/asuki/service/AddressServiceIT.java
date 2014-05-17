package org.asuki.service;

import static org.asuki.model.entity.Address.builder;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.asuki.model.entity.Address;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import org.jboss.arquillian.testng.Arquillian;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AddressServiceIT extends Arquillian {

    private static final String EAR_FILE_NAME = "test.ear";
    private static final String WAR_FILE_NAME = "test.war";

    // TODO 共通化
    @Deployment
    public static EnterpriseArchive deployment() {

        EnterpriseArchive ear = ShrinkWrap
                .create(EnterpriseArchive.class, EAR_FILE_NAME)
                .addAsModule(createWarArchive())
                .addAsApplicationResource(
                        "META-INF/jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml");

        System.out.println(ear.toString(true));
        // export(ear, format("/tmp/%s", EAR_FILE_NAME));

        return ear;
    }

    public static Archive<?> createWarArchive() {

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies().asFile();

        // @formatter:off
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, WAR_FILE_NAME)
                .addPackages(true, "org.asuki")
                .addAsWebInfResource(
                        "META-INF/beans.xml",
                        "beans.xml")
                .addAsWebInfResource(
                        "META-INF/xml/entity.xml",
                        "classes/META-INF/xml/entity.xml")
                .addAsWebInfResource(
                        "META-INF/xml/jpql.xml",
                        "classes/META-INF/xml/jpql.xml")
                .addAsWebInfResource(
                        "META-INF/xml/sql.xml",
                        "classes/META-INF/xml/sql.xml")
                .addAsWebInfResource(
                        "META-INF/testPersistence.xml",
                        "classes/META-INF/persistence.xml")
                .addAsLibraries(libs);
        // @formatter:on

        deleteConflictedLibraries(war);

        System.out.println(war.toString(true));

        return war;
    }

    private static void deleteConflictedLibraries(Archive<?> archive) {
        Map<ArchivePath, Node> content = archive.getContent(Filters
                .include("^/WEB-INF/lib/demo-.*\\.jar$"));
        for (ArchivePath ap : content.keySet()) {
            archive.delete(ap.get());
        }
    }

    @SuppressWarnings("unused")
    private static void export(Archive<? extends Archive<?>> archive,
            String path) {

        archive.as(ZipExporter.class).exportTo(new File(path), true);
    }

    // --------------------------------------下記はテストケース------------------------------------------

    @EJB
    private AddressService service;

    @BeforeMethod
    public void beforeMethod() throws Exception {

    }

    @Test
    public void shouldCrud() {

        List<Address> addresses = service.findAll();
        assertThat(addresses.size(), is(0));

        // @formatter:off
        Address expected = builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode("zipCode")
                .build();
        // @formatter:on

        expected = service.create(expected);

        addresses = service.findAll();
        assertThat(addresses.size(), is(1));

        Address actual = service.findById(expected.getId());
        assertThat(actual.toString(), is(expected.toString()));

    }

}
