package com.excilys.cdb.selenium;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.ressources.Action;
import com.excilys.cdb.ressources.JspRessources;
import com.excilys.cdb.testconfig.TestSpringConfig;

public class SeleniumSuite {
	
	protected static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringConfig.class);
    protected static JavascriptExecutor je;
    protected static WebDriver driver;
    private static int nombreClients;
    protected static final int PORT = 8080;
    
    private static final String ADMIN_TEST_NAME = "admin";
    private static final String ADMIN_TEST_PASSWORD = "admin@123";
    /**
     * Constructor des variables partagés des tests selenium, ici avec le singleton driver.
     */
    public SeleniumSuite() {
        if (driver == null) {
            ResourceBundle config = ResourceBundle.getBundle("config");
            System.setProperty("webdriver.gecko.driver", config.getString("driverPath"));
            driver = new FirefoxDriver();
            je = (JavascriptExecutor) driver;
    		MessageHandler.init(context);
    		
    		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
    		driver.get("http://localhost:" + PORT + "/webapp/computer/" + Action.LOGIN_FORM.getValue());
    		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
    		driver.findElement(By.id(JspRessources.FORM_LOGIN_USERNAME)).sendKeys(ADMIN_TEST_NAME);
    		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
    		driver.findElement(By.id(JspRessources.FORM_LOGIN_PASSWORD)).sendKeys(ADMIN_TEST_PASSWORD);
    		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
    		driver.findElement(By.id(JspRessources.BUTTON_LOGIN)).click();
        }
        nombreClients++;
    }

    /**
     * try to close instance, after waiting for a new test client.
     */
    public void closeInstance() {
        nombreClients--;

        //Si on a pas besoin du driver après 3 secondes, on le kill.
        if (nombreClients == 0) {
            new Thread(new Runnable() { public void run() {
                try {
                    Thread.sleep(3000);
                    if (nombreClients == 0) {
                        driver.quit();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } }).run();
        }
    }

}
