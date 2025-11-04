package com.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumBy;

public class WorkingLoginTest extends BaseTest {
    
    @BeforeMethod
    public void pageSetup() {
        // Add initial wait for app to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, description = "Verify user can login with valid credentials - Working Version")
    public void testValidLoginWorking() {
        System.out.println("=== Running Working Login Test ===");
        
        // Step 1: Enter credentials (using exact same approach that worked)
        System.out.println("1. Entering credentials...");
        WebElement emailField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Email']"));
        emailField.click();
        emailField.clear();
        emailField.sendKeys("nuwanthapiumal57@gmail.com");
        
        WebElement passwordField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Password']"));
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys("Nuwantha@1234");
        
        System.out.println("✓ Credentials entered");
        
        // Step 2: Submit the form
        System.out.println("2. Submitting login form...");
        WebElement signInButton = driver.findElement(AppiumBy.accessibilityId("Sign In"));
        signInButton.click();
        System.out.println("✓ Sign In button clicked");
        
        // Step 3: Wait for authentication (use same timing that worked)
        System.out.println("3. Waiting for authentication response...");
        try {
            Thread.sleep(15000); // Wait 15 seconds exactly like successful test
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Step 4: Check for home screen indicators (use exact same logic)
        System.out.println("4. Checking for home screen...");
        boolean isOnHomeScreen = false;
        
        try {
            // Try to find "Welcome Back" - if found, still on login
            driver.findElement(AppiumBy.accessibilityId("Welcome Back"));
            System.out.println("❌ Still on login screen");
            isOnHomeScreen = false;
        } catch (Exception e) {
            System.out.println("✅ No longer on login screen");
            
            // Look for home screen indicators
            try {
                driver.findElement(AppiumBy.accessibilityId("Dashboard"));
                System.out.println("✅ Dashboard found");
                isOnHomeScreen = true;
            } catch (Exception ex) {
                try {
                    driver.findElement(AppiumBy.accessibilityId("Quick Actions"));
                    System.out.println("✅ Quick Actions found");
                    isOnHomeScreen = true;
                } catch (Exception ex2) {
                    // Check if page contains user info
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains("nuwanthapiumal57@gmail.com") && pageSource.contains("Dashboard")) {
                        System.out.println("✅ User info and Dashboard found in page source");
                        isOnHomeScreen = true;
                    }
                }
            }
        }
        
        System.out.println("5. Final result: isOnHomeScreen = " + isOnHomeScreen);
        
        // Assertion
        Assert.assertTrue(isOnHomeScreen, 
            "User should be logged in and home screen should be displayed");
        
        System.out.println("🎉 LOGIN TEST PASSED!");
    }
}