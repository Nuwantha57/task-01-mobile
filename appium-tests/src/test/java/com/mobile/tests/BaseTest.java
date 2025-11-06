package com.mobile.tests;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.mobile.utils.ConfigReader;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

/**
 * Base test class with setup and teardown
 */
public class BaseTest {
    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException, URISyntaxException {
        // Configure UiAutomator2 options
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(ConfigReader.getDeviceName());
        options.setPlatformVersion(ConfigReader.getPlatformVersion());
        options.setApp(ConfigReader.getAppPath());
        options.setAutomationName("UiAutomator2");
        options.setNewCommandTimeout(Duration.ofSeconds(300));
        options.setNoReset(true); // Don't reset app state - keep user logged in
        options.setFullReset(false); // Don't uninstall app
        
        // Initialize driver using URI (non-deprecated way for Java 20+)
        URI appiumUri = new URI(ConfigReader.getAppiumUrl());
        driver = new AndroidDriver(appiumUri.toURL(), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Wait helper method
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
