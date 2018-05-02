package com.excilys.cdb.selenium;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.service.ComputerServiceImpl;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;

public class NewIT {

    private WebDriver driver;
    private IComputerService service;

    /**
     * @throws DAOConfigurationException service exception
     */
    @BeforeClass
    public void beforeClass() throws DAOConfigurationException {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("webdriver.firefox.port", 9090);
        driver = new FirefoxDriver(profile);
        service = ComputerServiceImpl.getInstance();
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    /**f.
     * @throws ServiceException fesf
     */
    @Test
    public void verifySearchButton() throws ServiceException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer");
        WebElement text = driver.findElement(By.name("test"));
        assertTrue(text.getText().equals("Test"));
        System.out.println(service.getAllComputer().get(service.getAllComputer().size() - 1).getName());
        assertTrue(service.getAllComputer().get(service.getAllComputer().size() - 1).getName().equals("fesfes"));
    }

}