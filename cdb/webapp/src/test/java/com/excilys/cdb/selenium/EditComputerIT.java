package com.excilys.cdb.selenium;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

import com.excilys.cdb.controllermessage.ControllerMessage;
import com.excilys.cdb.defaultvalues.DefaultValues;
import com.excilys.cdb.messagehandler.CDBMessage;
import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.ressources.Action;
import com.excilys.cdb.ressources.JspRessources;
import com.excilys.cdb.service.exceptions.ServiceException;
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
	public void beforeClass() throws ServiceException {
		LOGGER.info("BeforeClass in IT ajouter computer");
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.LIST_COMPUTERS.getValue() + "?page=1&limit="
				+ DefaultValues.DEFAULT_LIMIT);

		LOGGER.info("BeforeClass click gestion start");
		List<WebElement> elem = driver.findElements(By.xpath("//table/tbody/tr/td/a"));
		for (WebElement elems : elem) {
			LOGGER.info("Result by lign : " + elems.getText());
		}
		elem.get(0).click();
		LOGGER.info("BeforeClass click effectued");
		String idS = driver.getCurrentUrl().replace(
				"http://localhost:" + PORT + "/webapp/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id=", "");
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
		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Computer.PATTERN_DATE);
		String date = introduced.format(formatter);
		je.executeScript("return document.getElementById('introduced').value = '" + date + "';");
		Assert.assertEquals((String) je.executeScript("return document.getElementById('introduced').value;"), date);
	}

	/**
	 * Method used in editComputer.
	 * @param discontinued date discontinued à insérer.
	 */
	public void editTextDiscontinued(LocalDate discontinued) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Computer.PATTERN_DATE);
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
	public void verifyEditerbuttonEditOk() throws ServiceException  {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
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
		Assert.assertEquals(result, MessageHandler.getMessage(ControllerMessage.SUCCESS_UPDATE, null));
	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditFailForIntroduced() throws ServiceException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextIntroduced(Computer.BEGIN_DATE_VALID.minus(Period.ofDays(1)));
		driver.findElement(By.id(JspRessources.BUTTON_EDIT)).click();
		String result = "";
		try {
			Thread.sleep(300L);
			result = driver.findElement(By.id(JspRessources.ERROR)).getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(result,MessageHandler.getMessage(ControllerMessage.VALIDATION_DATE_INTRODUCED,
				Arrays.asList(Computer.BEGIN_DATE_VALID, Computer.END_DATE_VALID).toArray()));
	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditFailForDiscontinued() throws ServiceException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextDiscontinued(Computer.END_DATE_VALID.plus(Period.ofDays(1)));
		driver.findElement(By.id(JspRessources.BUTTON_EDIT)).click();
		String result = "";
		try {
			Thread.sleep(300L);
			result = driver.findElement(By.id(JspRessources.ERROR)).getText();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(result,MessageHandler.getMessage(ControllerMessage.VALIDATION_DATE_DISCONTINUED,
				Arrays.asList(Computer.BEGIN_DATE_VALID, Computer.END_DATE_VALID).toArray()));
	}

	/**
	 * Verify click du bouton + edit fonctionnel.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void verifyEditerbuttonEditFailForDiscontinuedBeforeIntroduced() throws ServiceException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.EDIT_FORM_COMPUTER.getValue() + "?id="
				+ edit.getId());
		this.editTextIntroduced(Computer.BEGIN_DATE_VALID.plus(Period.ofDays(3)));
		this.editTextDiscontinued(Computer.BEGIN_DATE_VALID.plus(Period.ofDays(1)));
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
		Assert.assertEquals(result, MessageHandler.getMessage(ControllerMessage.COMPUTER_INTRODUCED_AFTER, null));
	}

}