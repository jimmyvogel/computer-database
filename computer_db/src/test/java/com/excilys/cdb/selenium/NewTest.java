package com.excilys.cdb.selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewTest {

    private WebDriver driver;

    /**
     */
    @BeforeClass
    public void beforeClass() {
        driver = new FirefoxDriver();
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    /**
     */
    @Test
    public void verifySearchButton() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:4040/computer_db/");
        WebElement searchButton = driver.findElement(By.name("btnK"));

        String text = searchButton.getAttribute("value");

        Assert.assertEquals(text, " ", "Text not found!");
    }

}