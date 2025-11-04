package com.mobile.tests;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {
    protected AndroidDriver driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554");
        options.setApp("C:/Intern_Project_Code/task-01-mobile/build/app/outputs/apk/debug/app-debug.apk");
        options.setAutomationName("UiAutomator2");
        options.setPlatformName("Android");
        options.setPlatformVersion("16");
        options.setNewCommandTimeout(Duration.ofSeconds(300));
        options.setNoReset(false);

        driver = new AndroidDriver(
            URI.create("http://127.0.0.1:4723").toURL(), 
            options
        );
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
