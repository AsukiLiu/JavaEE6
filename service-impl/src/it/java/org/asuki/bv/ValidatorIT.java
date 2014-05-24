package org.asuki.bv;

import static com.google.common.collect.Iterables.toArray;
import static org.asuki.common.Constants.Systems.LINE_SEPARATOR;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.fail;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.asuki.bv.InputBean;
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

import com.google.common.base.Splitter;

public class ValidatorIT extends Arquillian {

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
        // export(ear, String.format("/tmp/%s", EAR_FILE_NAME));

        return ear;
    }

    private static Archive<?> createWarArchive() {

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies().asFile();

        // @formatter:off
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, WAR_FILE_NAME)
                .addPackages(true, "org.asuki")
                .addAsWebInfResource(
                        "META-INF/beans.xml",
                        "beans.xml")
                .addAsResource(
                        "ValidationMessages_en.properties",
                        "ValidationMessages_en.properties")
                .addAsResource(
                        "ValidationMessages_ja.properties",
                        "ValidationMessages_ja.properties")
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

    @Inject
    private CustomValidator customValidator;

    @BeforeMethod
    public void beforeMethod() throws Exception {

    }

    @Test
    public void testValidator() {
        InputBean ib = new InputBean();
        ib.setData("aaabbbccc");

        // @formatter:off
        List<InputSubBean> beans = Arrays.asList(
                new InputSubBean(new BigDecimal("1.2")),
                new InputSubBean(new BigDecimal(12345)), 
                new InputSubBean(new BigDecimal(123456)));
        // @formatter:on
        ib.setBeans(beans);

        try {
            customValidator.validate(ib);
        } catch (Exception e) {

            Iterable<String> messages = Splitter.on(LINE_SEPARATOR)
                    .trimResults().split(e.getMessage());

            assertThat(toArray(messages, String.class).length, is(4));
            return;
        }

        fail("No exception happened!");
    }

}
