package org.asuki.test.web;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@RunAsClient
public class DeltaSpikeIT extends Arquillian {

    private static final String TEST_DEPLOYMENT = "target/demo-web.war";

    private static final String EXPECTED_MSG = "Host[127.0.0.1:8180], Path[/demo-web], Name[Andy]";
    private static final String IN_NAME = "Andy";

    @FindBy(id = "msg")
    private WebElement MESSAGE;

    @FindByJQuery("input[id *= 'name']")
    private WebElement NAME;

    @FindByJQuery("input[value = 'Submit']")
    private WebElement SUBMIT;

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(
                TEST_DEPLOYMENT));
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get(contextPath.toString() + "deltaspike.xhtml");
    }

    @Test
    public void testServletModule() {

        NAME.sendKeys(IN_NAME);
        guardHttp(SUBMIT).click();

        assertThat(EXPECTED_MSG, is(MESSAGE.getText().trim()));
    }

}
