package org.asuki.test.web;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WebDriverTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setUpBeforeClass() {
        driver = new FirefoxDriver();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        driver.quit();
    }

    @Test
    public void shouldSendText() throws InterruptedException {
        final String expected = "test";

        driver.get("http://localhost:8080/demo-web/");

        driver.findElement(By.id("form:inputText")).sendKeys(expected);
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.id("form:sendButton")).click();

        assertThat(driver.getTitle(), is("Success page"));
        assertThat(driver.findElement(By.id("sentText")).getText(),
                is(expected));
        TimeUnit.SECONDS.sleep(2);
    }
}
