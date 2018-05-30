package com.excilys.cdb.selenium;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.Action;
import com.excilys.cdb.servlet.ressources.JspRessources;
import com.excilys.cdb.validator.ComputerValidator;

public class AjouterComputerIT extends SeleniumSuite {

	//@Autowired
    //private static CompanyService serviceCompany;
    private static Computer ajout;
	private static final Logger LOGGER = LoggerFactory.getLogger(AjouterComputerIT.class);
    private static final String NAMEINVALID = "";

    /** Beforeclass.
     * @throws DaoConfigurationException configuration exception
     * @throws ServiceException service exception
     */
    @BeforeClass
    public static void beforeClass() throws DaoConfigurationException, ServiceException {
    	LOGGER.info("BeforeClass in IT ajouter computer");
        ajout = new Computer("nameValid",
                ComputerValidator.BEGIN_DATE_VALID.plus(Period.ofDays(1)),
                ComputerValidator.END_DATE_VALID.minus(Period.ofDays(1)),
                new Company(2, "Thinking Machines")); //serviceCompany.getAllCompany().get(1));
    }

    /**
     */
    @AfterClass
    public void afterClass() {
    	LOGGER.info("AfterClass in IT ajouter computer");
        this.closeInstance();
    }

    /**
     */

    /** Method used in ajoutComputer.
     * @param name name à insérer
     */
    private void ajoutTextComputerName(String name) {
    	LOGGER.info("ajoutTextcomputerName : " + name);
        driver.findElement(By.id("computerName")).sendKeys(name);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        assertEquals(name, driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_NAME)).getAttribute("value"));
        LOGGER.info("fin ajoutTextcomputerName");
    }

    /** Method used in ajoutComputer.
     * @param introduced la date à insérer
     */
    private void ajoutTextIntroduced(LocalDate introduced) {
    	LOGGER.info("ajoutTextcomputerIntroduced : " + introduced);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = introduced.format(formatter);
        je.executeScript("return document.getElementById('introduced').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('introduced').value;"), date);
        LOGGER.info("fin ajoutTextcomputerIntroduced");
    }

    /** Method used in ajoutComputer.
     * @param discontinued date discontinued à insérer.
     */
    public void ajoutTextDiscontinued(LocalDate discontinued) {
    	LOGGER.info("ajoutTextDiscontinued : " + discontinued);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = discontinued.format(formatter);
        je.executeScript("return document.getElementById('discontinued').value = '" + date + "';");
        Assert.assertEquals((String) je.executeScript("return document.getElementById('discontinued').value;"), date);
        LOGGER.info("fin ajoutTextDiscontinued");
    }

    /** Method used in ajoutComputer.
     * @param company la company lié à choisir.
     */
    private void ajoutTextCompanyIdName(Company company) {
    	LOGGER.info("ajoutTextCompanyIdName : " + company);
        if (company != null) {
        	LOGGER.info("Passage dans le if");
            WebElement select = driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY));
            LOGGER.info("Récupération de l'idcompany");
            List<WebElement> options = select.findElements(By.tagName("option"));
            LOGGER.info("Récupération de l'élément by tag");
            for (WebElement option : options) {
                if (option.getText().equals(company.getName())) {
                    option.click();
                    break;
                }
            }
            LOGGER.info("Juste après la sélection de l'élément");
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(company.getId()), Long.valueOf(driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY)).getAttribute("value")));

        } else {
            driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
            Assert.assertEquals(new Long(-1), Long.valueOf(driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY)).getAttribute("value")));
        }
        LOGGER.info(" fin ajoutTextCompanyIdName : ");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutOk() throws ServiceException, DaoException {
    	LOGGER.info("ajoutButtonAjoutOk");
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.ADD_FORM_COMPUTER.getValue());
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getIntroduced());
        this.ajoutTextDiscontinued(ajout.getDiscontinued());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        LOGGER.info("Juste avant le bouton add");
        driver.findElement(By.id(JspRessources.BUTTON_ADD)).click();
        String result = "";
        LOGGER.info("Button add ok Modification work: test result");
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id(JspRessources.SUCCESS)).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Result: " + result);
        Assert.assertEquals(result, "Create Computer success.");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForName() throws ServiceException, DaoException {
    	LOGGER.info("verifyAjouterButtonAjoutFailForName");
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.ADD_FORM_COMPUTER.getValue());
        this.ajoutTextComputerName(NAMEINVALID);
        this.ajoutTextIntroduced(ajout.getIntroduced());
        this.ajoutTextDiscontinued(ajout.getDiscontinued());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id(JspRessources.BUTTON_ADD)).click();
        String result = "";
        LOGGER.info("Modification work: test result");
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id(JspRessources.ERROR)).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Result: " + result);
        Assert.assertEquals(result, "Le nom : " + NAMEINVALID + "est invalid car : Le nom est trop court 3 lettres minimum");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForIntroduced() throws ServiceException, DaoException {
    	LOGGER.info("verifyAjouterButtonAjoutFailForIntroduced");
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.ADD_FORM_COMPUTER.getValue());
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getIntroduced().minus(Period.ofYears(100)));
        this.ajoutTextDiscontinued(ajout.getDiscontinued());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id(JspRessources.BUTTON_ADD)).click();
        String result = "";
        LOGGER.info("Modification work: test result");
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id(JspRessources.ERROR)).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Result: " + result);
        Assert.assertEquals(result, "La date est invalid car : Introduced date n'est pas comprise entre 1972-12-31 et 2030-01-01");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForDiscontinued() throws ServiceException, DaoException {
    	LOGGER.info("verifyAjouterButtonAjoutFailForDiscontinued");
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.ADD_FORM_COMPUTER.getValue());
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getIntroduced());
        this.ajoutTextDiscontinued(ajout.getDiscontinued().minus(Period.ofYears(100)));
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id(JspRessources.BUTTON_ADD)).click();
        String result = "";
        LOGGER.info("Modification work: test result");
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id(JspRessources.ERROR)).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Result: " + result);
        Assert.assertEquals(result, "La date est invalid car : Discontinued date n'est pas comprise entre 1972-12-31 et 2030-01-01");
    }

    /**Verify click du bouton + ajout fonctionnel.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void verifyAjouterButtonAjoutFailForDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {
    	LOGGER.info("verifyAjouterButtonAjoutFailForDiscontinuedBeforeIntroduced");
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.ADD_FORM_COMPUTER.getValue());
        this.ajoutTextComputerName(ajout.getName());
        this.ajoutTextIntroduced(ajout.getDiscontinued());
        this.ajoutTextDiscontinued(ajout.getIntroduced());
        this.ajoutTextCompanyIdName(ajout.getCompany());
        driver.findElement(By.id(JspRessources.BUTTON_ADD)).click();
        String result = "";
        LOGGER.info("Modification work: test result");
        try {
            Thread.sleep(300L);
            result = driver.findElement(By.id(JspRessources.ERROR)).getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Result: " + result);
        Assert.assertEquals(result, "La date est invalid car : Computer introduced is after computer discontinued.");
    }

}