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
        // Start each run from a clean state so tests begin at Login screen
        options.setNoReset(false);
        options.setFullReset(false);
        // Improve app start stability on cold launches
        try {
            // These are best-effort; ignore if not supported by this client version
            options.setAppWaitActivity("*");
        } catch (Exception ignored) { }
        
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
