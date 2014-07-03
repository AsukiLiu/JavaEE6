package org.asuki.test.web;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

import org.asuki.test.BaseArquillian;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@RunAsClient
public class WebDriverIT extends BaseArquillian {

    static {
        jarFilterRegexp += "|^/WEB-INF/lib/resteasy-.*\\.jar$";
    }

    @ArquillianResource
    private URL deploymentUrl;

    private static WebDriver driver;

    @Drone
    private WebDriver droneDriver;

    @BeforeClass
    public static void setUpBeforeClass() {
        driver = new FirefoxDriver();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        driver.quit();
    }

    @Test
    public void shouldSendText() {
        testWebDriver(driver);
    }

    @Test
    public void shouldSendTextByDrone() {
        testWebDriver(droneDriver);
    }

    @SneakyThrows
    private void testWebDriver(WebDriver webDriver) {
        final String expected = "test";

        // Change default url
        webDriver.get(deploymentUrl.toString().replace("test", "demo-web"));

        webDriver.findElement(By.id("form:inputText")).sendKeys(expected);
        TimeUnit.SECONDS.sleep(2);
        webDriver.findElement(By.id("form:sendButton")).click();

        assertThat(webDriver.getTitle(), is("Success page"));
        assertThat(webDriver.findElement(By.id("sentText")).getText(),
                is(expected));
        TimeUnit.SECONDS.sleep(2);
    }

}
