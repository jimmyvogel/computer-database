package com.excilys.cdb.selenium;

import static org.testng.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.ComputerValidator;

public class AjouterComputerIT extends SeleniumSuite {

	@Autowired
    private CompanyService serviceCompany;
    private Computer ajout;

    private static final String NAMEINVALID = "";

    /** Beforeclass.
     * @throws DAOConfigurationException configuration exception
     * @throws ServiceException service exception
     */
    @BeforeClass
    public void beforeClass() throws DAOConfigurationException, ServiceException {
        ajout = new Computer("nameValid",
                ComputerValidator.BEGIN_DATE_VALID.plus(Period.ofDays(1)),
                ComputerValidator.END_DATE_VALID.minus(Period.ofDays(1)),
                serviceCompany.getAllCompany().get(1));
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        this.closeInstance();
    }

    /**
     */

    /** Method used in ajoutComputer.
     * @param name name à insérer
     */
    private void ajoutTextComputerName(String name) {
        driver.findElement(By.id("computerName")).sendKeys(name);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        assertEquals(name, driver.findElement(By.id("computerName")).getAttribute("value"));

    }

    /** Method used in ajoutComputer.
     * @param introduced la date à insérer
     */
    private void ajoutTextIntroduced(LocalDateTime introduced) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = introduced.format(formatter);
        je.executeScript("return document.getElementById('introduced').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('introduced').value;"), date);
    }

    /** Method used in ajoutComputer.
     * @param discontinued date discontinued à insérer.
     */
    public void ajoutTextDiscontinued(LocalDateTime discontinued) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = discontinued.format(formatter);
        je.executeScript("return document.getElementById('discontinued').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('discontinued').value;"), date);
    }

    /** Method used in ajoutComputer.
     * @param company la company lié à choisir.
     */
    private void ajoutTextCompanyIdName(Company company) {

        if (company != null) {
            WebElement select = driver.findElement(By.id("companyId"));
            List<WebElement> options = select.findElements(By.tagName("option"));
            for (WebElement option : options) {
                if (option.getText().equals(company.getName())) {
                    option.click();
                    break;
                }
            }
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(company.getId()), Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value")));

        } else {
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(-1), Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value")));
        }

    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutOk() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=add_form_computer");
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getIntroduced());
        this.ajoutTextDiscontinued(ajout.getDiscontinued());
        this.ajoutTextCompanyIdName(ajout.getCompany());
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

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForName() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=add_form_computer");
        this.ajoutTextComputerName(NAMEINVALID);
        this.ajoutTextIntroduced(ajout.getIntroduced());
        this.ajoutTextDiscontinued(ajout.getDiscontinued());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id("buttonAjout")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "Le nom : " + NAMEINVALID + "est invalid car : Le nom est trop court 3 lettres minimum");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForIntroduced() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=add_form_computer");
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getIntroduced().minus(Period.ofYears(100)));
        this.ajoutTextDiscontinued(ajout.getDiscontinued());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id("buttonAjout")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "La date est invalid car : introduced date n'est pas comprise entre 1972-12-31 et 2030-01-01");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForDiscontinued() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=add_form_computer");
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getIntroduced());
        this.ajoutTextDiscontinued(ajout.getDiscontinued().minus(Period.ofYears(100)));
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id("buttonAjout")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(result, "La date est invalid car : discontinued date n'est pas comprise entre 1972-12-31 et 2030-01-01");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:9090/computer_db/computer?action=add_form_computer");
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getDiscontinued());
        this.ajoutTextDiscontinued(ajout.getIntroduced());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id("buttonAjout")).click();
        String result = "";
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id("error")).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Assert.assertEquals(result, "La date est invalid car : Computer introduced is after computer discontinued: " + ajout.getIntroduced().format(formatter));
    }

}