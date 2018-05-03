package com.excilys.cdb.selenium;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;

public class AjouterComputerIT {

    private WebDriver driver;
    private ComputerService service;
    private Computer ajout;

    /** Beforeclass.
     * @throws DAOConfigurationException configuration exception
     * @throws ServiceException service exception
     */
    @BeforeClass
    public void beforeClass() throws DAOConfigurationException, ServiceException {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("webdriver.firefox.port", 9090);
        driver = new FirefoxDriver();
        service = ComputerService.getInstance();
        ajout = service.getPageComputer(1).getObjects().get(1);
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException fesf
     */
    @Test
    public void ajoutTextComputerName() throws ServiceException {

        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=ADD_FORM_COMPUTER");
        driver.findElement(By.id("computerName")).sendKeys(ajout.getName());
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        System.out.println(driver.findElement(By.id("computerName")).getAttribute("value"));
        assertEquals(ajout.getName(), driver.findElement(By.id("computerName")).getAttribute("value"));

    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException fesf
     */
    @Test
    public void ajoutTextIntroduced() throws ServiceException {

        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=ADD_FORM_COMPUTER");

        if (ajout.getIntroduced() != null) {
            System.out.println(ajout.getIntroduced());
            driver.findElement(By.id("introduced")).sendKeys(ajout.getIntroduced().toString());
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            System.out.println(driver.findElement(By.id("introduced")).getAttribute("value"));
            assertEquals(ajout.getIntroduced().toString(), driver.findElement(By.id("introduced")).getAttribute("value"));
        }

    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException fesf
     */
    @Test
    public void ajoutTextDiscontinued() throws ServiceException {

        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=ADD_FORM_COMPUTER");

        if (ajout.getDiscontinued() != null) {
            System.out.println(ajout.getDiscontinued());
            driver.findElement(By.id("discontinued")).sendKeys(ajout.getDiscontinued().toString());
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            System.out.println(driver.findElement(By.id("discontinued")).getAttribute("value"));
            assertEquals(ajout.getDiscontinued().toString(), driver.findElement(By.id("discontinued")).getAttribute("value"));
        }

    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException fesf
     */
    @Test
    public void ajoutTextCompanyIdName() throws ServiceException {

        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=ADD_FORM_COMPUTER");
        if (ajout.getCompany() != null) {
            WebElement select = driver.findElement(By.id("companyId"));
            List<WebElement> options = select.findElements(By.tagName("option"));
            for (WebElement option : options) {
                if (option.getText().equals(ajout.getCompany().getName())) {
                    option.click();
                    break;
                }
            }
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(ajout.getCompany().getId()), Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value")));

        } else {
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(-1), Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value")));
        }

    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException fesf
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButton() throws ServiceException, DaoException {

        int nombres = (int) service.countComputers();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=ADD_FORM_COMPUTER");

        driver.findElement(By.id("computerName")).sendKeys(ajout.getName());
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);

        driver.findElement(By.name("buttonAjout")).click();

        assertEquals((int) service.countComputers(), nombres + 1);

    }

}