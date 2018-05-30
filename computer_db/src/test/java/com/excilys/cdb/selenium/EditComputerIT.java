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
import com.excilys.cdb.servlet.ressources.Action;
import com.excilys.cdb.servlet.ressources.DefaultValues;
import com.excilys.cdb.servlet.ressources.JspRessources;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.ComputerValidator;
import com.excilys.cdb.validator.DateValidation;

public class EditComputerIT extends SeleniumSuite {

	private Computer edit;
	private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerIT.class);

	/**
	 * Beforeclass.
	 * @throws DaoConfigurationException configuration exception
	 * @throws ServiceException service exception
	 */
	@BeforeClass
	public void beforeClass() throws DaoConfigurationException, ServiceException {
		LOGGER.info("BeforeClass in IT ajouter computer");
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.LIST_COMPUTERS.getValue() + "?page=1&limit="
				+ DefaultValues.DEFAULT_LIMIT);

		LOGGER.info("BeforeClass click gestion start");
		List<WebElement> elem = driver.findElements(By.xpath("//table/tbody/tr/td/a"));
		for (WebElement elems : elem) {
			LOGGER.info("Result by lign : " + elems.getText());
		}
		elem.get(0).click();
		LOGGER.info("BeforeClass click effectued");
		String idS = driver.getCurrentUrl().replace(
				"http://localhost:" + PORT + "/computer_db/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id=", "");
		Integer id = Integer.valueOf(idS.trim());
		LOGGER.info("BeforeClass id récupéré");
		String name = driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_NAME)).getAttribute("value");
		LOGGER.info("BeforeClass name récupéré");
		String introduced = (String) je.executeScript("return document.getElementById('introduced').value;");
		LOGGER.info("BeforeClass introduced récupéré");
		String discontinued = (String) je.executeScript("return document.getElementById('discontinued').value;");
		LOGGER.info("BeforeClass discontinued récupéré");
		Long idCompany = Long.valueOf(driver.findElement(By.id("companyId")).getAttribute("value"));
		LOGGER.info("Récupération des valeurs initiales + Creation de la company de base");
		Company c = new Company(idCompany, "Nom Company inutile");
		edit = new Computer(id, name, DateValidation.validDateFormat(introduced),
				DateValidation.validDateFormat(discontinued), c);
		LOGGER.info("BeforeClass fin");
	}

	/**
	 */
	@AfterClass
	public void afterClass() {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextComputerName(edit.getName());
		if (edit.getIntroduced() != null) {
			this.editTextIntroduced(edit.getIntroduced());
		}
		if (edit.getDiscontinued() != null) {
			this.editTextDiscontinued(edit.getDiscontinued());
		}
		this.editTextCompanyIdName(edit.getCompany());
		driver.findElement(By.id("buttonEdit")).click();
		this.closeInstance();
	}

	/**
	 * Method used in editComputer.
	 * @param name name à insérer
	 */
	private void editTextComputerName(String name) {
		driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_NAME)).sendKeys(name);
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		assertEquals(name, driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_NAME)).getAttribute("value"));

	}

	/**
	 * Method used in editComputer.
	 * @param introduced la date à insérer
	 */
	private void editTextIntroduced(LocalDate introduced) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = introduced.format(formatter);
		je.executeScript("return document.getElementById('introduced').value = '" + date + "';");
		Assert.assertEquals((String) je.executeScript("return document.getElementById('introduced').value;"), date);
	}

	/**
	 * Method used in editComputer.
	 * @param discontinued date discontinued à insérer.
	 */
	public void editTextDiscontinued(LocalDate discontinued) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = discontinued.format(formatter);
		je.executeScript("return document.getElementById('discontinued').value = '" + date + "';");
		Assert.assertEquals((String) je.executeScript("return document.getElementById('discontinued').value;"), date);
	}

	/**
	 * Method used in editComputer.
	 * @param company la company lié à choisir.
	 */
	private void editTextCompanyIdName(Company company) {

		if (company != null) {
			WebElement select = driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY));
			List<WebElement> options = select.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if (option.getText().equals(company.getName())) {
					option.click();
					break;
				}
			}
			driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
			Assert.assertEquals(new Long(company.getId()), Long.valueOf(
					driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY)).getAttribute("value")));

		} else {
			driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
			Assert.assertEquals(new Long(-1), Long.valueOf(
					driver.findElement(By.id(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY)).getAttribute("value")));
		}

	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditOk() throws ServiceException, DaoException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextComputerName(edit.getName() + "modifier");
		driver.findElement(By.id(JspRessources.BUTTON_EDIT)).click();
		String result = "";
		try {
			Thread.sleep(300L);
			result = driver.findElement(By.id(JspRessources.SUCCESS)).getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(result, "Update Computer success.");
	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditFailForIntroduced() throws ServiceException, DaoException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextIntroduced(ComputerValidator.BEGIN_DATE_VALID.minus(Period.ofDays(1)));
		driver.findElement(By.id(JspRessources.BUTTON_EDIT)).click();
		String result = "";
		try {
			Thread.sleep(300L);
			result = driver.findElement(By.id(JspRessources.ERROR)).getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(result,
				"La date est invalid car : Introduced date n'est pas comprise entre 1972-12-31 et 2030-01-01");
	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditFailForDiscontinued() throws ServiceException, DaoException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextDiscontinued(ComputerValidator.END_DATE_VALID.plus(Period.ofDays(1)));
		driver.findElement(By.id(JspRessources.BUTTON_EDIT)).click();
		String result = "";
		try {
			Thread.sleep(300L);
			result = driver.findElement(By.id(JspRessources.ERROR)).getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(result,
				"La date est invalid car : Discontinued date n'est pas comprise entre 1972-12-31 et 2030-01-01");
	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditFailForDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/computer_db/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextIntroduced(ComputerValidator.BEGIN_DATE_VALID.plus(Period.ofDays(3)));
		this.editTextDiscontinued(ComputerValidator.BEGIN_DATE_VALID.plus(Period.ofDays(1)));
		driver.findElement(By.id(JspRessources.BUTTON_EDIT)).click();
		String result = "";
		try {
			Thread.sleep(300L);
			if (driver.findElement(By.id(JspRessources.ERROR)) == null) {
				Assert.fail("Element pas trouvé");
			}
			result = driver.findElement(By.id(JspRessources.ERROR)).getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(result, "La date est invalid car : Computer introduced is after computer discontinued.");
	}

}