package org.asuki.test;

import static org.testng.collections.Maps.newHashMap;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.persistence20.PersistenceDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public abstract class BaseArquillian extends Arquillian {

    private static final String EAR_FILE_NAME = "test.ear";
    private static final String WAR_FILE_NAME = "test.war";

    protected static Map<String, String> webInfResources = newHashMap();
    protected static Map<String, String> resources = newHashMap();
    protected static PersistenceDescriptor persistenceXml;
    protected static String jarFilterRegexp = "^/WEB-INF/lib/demo-.*\\.jar$";

    @Deployment
    public static EnterpriseArchive deployment() {

        EnterpriseArchive ear = ShrinkWrap
                .create(EnterpriseArchive.class, EAR_FILE_NAME)
                .addAsModule(createWarArchive())
                .addAsApplicationResource(
                        "META-INF/jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml");

        // System.out.println(ear.toString(true));
        // export(ear, String.format("/tmp/%s", EAR_FILE_NAME));

        return ear;
    }

    private static Archive<?> createWarArchive() {

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies().resolve()
                .withTransitivity().asFile();

        // @formatter:off
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, WAR_FILE_NAME)
                .addPackages(true, "org.asuki")
                .addAsWebInfResource(
                        "META-INF/beans.xml",
                        "beans.xml")
                .addAsWebInfResource(
                        "META-INF/jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml")
                .addAsLibraries(libs);
        // @formatter:on

        for (Entry<String, String> entry : webInfResources.entrySet()) {
            war.addAsWebInfResource(entry.getKey(), entry.getValue());
        }

        for (Entry<String, String> entry : resources.entrySet()) {
            war.addAsResource(entry.getKey(), entry.getValue());
        }

        if (persistenceXml != null) {
            war.addAsWebInfResource(
                    new StringAsset(persistenceXml.exportAsString()),
                    "classes/META-INF/persistence.xml");
        }

        deleteConflictedLibraries(war);

        // System.out.println(war.toString(true));

        return war;
    }

    private static void deleteConflictedLibraries(Archive<?> archive) {

        Map<ArchivePath, Node> content = archive.getContent(Filters
                .include(jarFilterRegexp));

        for (ArchivePath ap : content.keySet()) {
            archive.delete(ap.get());
        }
    }

    @SuppressWarnings("unused")
    private static void export(Archive<? extends Archive<?>> archive,
            String path) {

        archive.as(ZipExporter.class).exportTo(new File(path), true);
    }

}
