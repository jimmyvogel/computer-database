package com.excilys.cdb.selenium;

import static org.testng.Assert.assertEquals;

import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.ComputerValidator;

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
        driver = new FirefoxDriver();
        service = ComputerService.getInstance();
        ajout = new Computer("nameValid",
                ComputerValidator.BEGIN_DATE_VALID.plus(Period.ofDays(1)),
                ComputerValidator.END_DATE_VALID.minus(Period.ofDays(1)),
                service.getAllCompany().get(1));
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=ADD_FORM_COMPUTER");
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    /** Method used in ajoutComputer.
     */
    private void ajoutTextComputerName() {
        driver.findElement(By.id("computerName")).sendKeys(ajout.getName());
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        assertEquals(ajout.getName(), driver.findElement(By.id("computerName")).getAttribute("value"));

    }

    /** Method used in ajoutComputer.
     * @param je javascript executor
     */
    private void ajoutTextIntroduced(JavascriptExecutor je) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = ajout.getIntroduced().format(formatter);
        je.executeScript("return document.getElementById('introduced').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('introduced').value;"), date);
    }

    /** Method used in ajoutComputer.
     * @param je javascript executor
     */
    public void ajoutTextDiscontinued(JavascriptExecutor je) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = ajout.getDiscontinued().format(formatter);
        je.executeScript("return document.getElementById('discontinued').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('discontinued').value;"), date);
    }

    /** Method used in ajoutComputer.
     */
    private void ajoutTextCompanyIdName() {

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
        JavascriptExecutor je = (JavascriptExecutor) driver;
        this.ajoutTextComputerName();
        this.ajoutTextIntroduced(je);
        this.ajoutTextDiscontinued(je);
        this.ajoutTextCompanyIdName();
        driver.findElement(By.id("buttonAjout")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("success")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "Create Computer " + ajout.getName() + " success.");
    }

}