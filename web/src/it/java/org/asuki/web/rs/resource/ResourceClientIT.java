package org.asuki.web.rs.resource;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

import org.asuki.web.rs.RestApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.annotations.Test;

@RunAsClient
public class ResourceClientIT extends Arquillian {

    private static final String EAR_FILE_NAME = "test.ear";
    private static final String WAR_FILE_NAME = "test.war";

    private static final String APPLICATION_PATH = RestApplication.class
            .getAnnotation(ApplicationPath.class).value().substring(1);

    // TODO 共通化
    @Deployment(testable = false)
    public static Archive<?> deployment() {

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
                .addAsLibraries(libs);
        // @formatter:on

        deleteConflictedLibraries(war);

        // System.out.println(war.toString(true));

        return war;
    }

    private static void deleteConflictedLibraries(Archive<?> archive) {

        Map<ArchivePath, Node> content = archive
                .getContent(Filters
                        .include("^/WEB-INF/lib/demo-.*\\.jar$|^/WEB-INF/lib/resteasy-.*\\.jar$"));

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

    @ArquillianResource
    private URL deploymentUrl;

    @Test
    public void testAddressResource() throws Exception {

        String uri = deploymentUrl.toString() + APPLICATION_PATH
                + "/address/get/1";

        ClientRequest request = new ClientRequest(uri);
        request.header("Accept", MediaType.APPLICATION_XML);

        ClientResponse<String> response = request.get(String.class);

        assertThat(response.getStatus(), is(200));

        // @formatter:off
        String content = response.getEntity()
                .replaceAll("<\\?xml.*\\?>", "")
                .replaceAll("<createdTime>.*</createdTime>", "")
                .replaceAll("<updatedTime>.*</updatedTime>", "")
                .trim();
        // @formatter:on

        assertThat(
                content,
                is("<address city=\"city\"><zipCode>zipCode</zipCode><prefecture>prefecture</prefecture></address>"));
    }

    @Test
    public void testDemoResource() throws Exception {

        String uri = deploymentUrl.toString() + APPLICATION_PATH
                + "/demo/position;latitude=37.12;longitude=165.26";

        ClientRequest request = new ClientRequest(uri);
        request.header("Accept", MediaType.TEXT_PLAIN);

        ClientResponse<String> response = request.get(String.class);

        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is("37.12N 165.26E"));
    }

}
