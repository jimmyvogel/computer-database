package com.excilys.cdb.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumSuite {

    protected static WebDriver driver;
    private static int nombreClients;

    /**
     * Constructor des variables partagés des tests selenium, ici avec le singleton driver.
     */
    public SeleniumSuite() {
        if (driver == null) {
            driver = new FirefoxDriver();
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
