package com.excilys.cdb.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.testconfig.TestSpringConfig;

public class SeleniumSuite {
	
	protected static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringConfig.class);
    protected static JavascriptExecutor je;
    protected static WebDriver driver;
    private static int nombreClients;
    protected static final int PORT = 8080;
    /**
     * Constructor des variables partagés des tests selenium, ici avec le singleton driver.
     */
    public SeleniumSuite() {
        if (driver == null) {
            driver = new FirefoxDriver();
            je = (JavascriptExecutor) driver;
    		MessageHandler.init(context);
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
