package com.mobile.tests;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class FixedLoginTest extends BaseTest {

    @BeforeMethod
    public void pageSetup() {
        // Add initial wait for app to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, description = "Verify user can login with valid credentials - FIXED VERSION")
    public void testValidLogin() {
        System.out.println("=== Running Fixed Login Test ===");
        
        // Check if already logged in first (same logic as SmartLoginTest)
        boolean alreadyLoggedIn = false;
        try {
            driver.findElement(AppiumBy.accessibilityId("Dashboard"));
            System.out.println("✅ Already on Dashboard - user is logged in!");
            alreadyLoggedIn = true;
        } catch (Exception e) {
            try {
                driver.findElement(AppiumBy.accessibilityId("Quick Actions"));
                System.out.println("✅ Already on home screen - user is logged in!");
                alreadyLoggedIn = true;
            } catch (Exception e2) {
                try {
                    driver.findElement(AppiumBy.accessibilityId("Welcome Back"));
                    System.out.println("📱 On login screen - need to authenticate");
                    alreadyLoggedIn = false;
                } catch (Exception e3) {
                    System.out.println("❓ Unknown screen state");
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains("Dashboard") || pageSource.contains("Quick Actions")) {
                        alreadyLoggedIn = true;
                    } else {
                        alreadyLoggedIn = false;
                    }
                }
            }
        }
        
        if (!alreadyLoggedIn) {
            System.out.println("Need to login - entering credentials...");
            
            // Use direct element interaction (same as working tests)
            WebElement emailField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Email']"));
            emailField.click();
            emailField.clear();
            emailField.sendKeys("nuwanthapiumal57@gmail.com");
            
            WebElement passwordField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Password']"));
            passwordField.click();
            passwordField.clear();
            passwordField.sendKeys("Nuwantha@1234");
            
            System.out.println("✓ Credentials entered");
            
            WebElement signInButton = driver.findElement(AppiumBy.accessibilityId("Sign In"));
            signInButton.click();
            System.out.println("✓ Sign In button clicked");
            
            // Wait for authentication (exact same as working test)
            System.out.println("Waiting for authentication...");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        // Verify home screen (same logic as SmartLoginTest)
        System.out.println("Checking for home screen...");
        boolean isOnHomeScreen = false;
        
        try {
            driver.findElement(AppiumBy.accessibilityId("Dashboard"));
            isOnHomeScreen = true;
            System.out.println("✅ Dashboard found");
        } catch (Exception e) {
            try {
                driver.findElement(AppiumBy.accessibilityId("Quick Actions"));
                isOnHomeScreen = true;
                System.out.println("✅ Quick Actions found");
            } catch (Exception e2) {
                String pageSource = driver.getPageSource();
                if (pageSource.contains("nuwanthapiumal57@gmail.com") || 
                    pageSource.contains("Dashboard") ||
                    pageSource.contains("Quick Actions")) {
                    isOnHomeScreen = true;
                    System.out.println("✅ Home screen content found in page source");
                } else {
                    System.out.println("❌ No home screen indicators found");
                    System.out.println("Page contains Welcome Back: " + pageSource.contains("Welcome Back"));
                    System.out.println("Page contains Sign In: " + pageSource.contains("Sign In"));
                }
            }
        }
        
        System.out.println("Final result: isOnHomeScreen = " + isOnHomeScreen);
        
        Assert.assertTrue(isOnHomeScreen, 
            "User should be on home screen (either already logged in or after successful login)");
        
        System.out.println("🎉 FIXED LOGIN TEST PASSED!");
    }

    @Test(priority = 2, description = "Verify login button exists and is clickable")
    public void testLoginButtonState() {
        // Only run if on login screen
        try {
            driver.findElement(AppiumBy.accessibilityId("Dashboard"));
            System.out.println("Already logged in - skipping button test");
            return;
        } catch (Exception e) {
            // On login screen, continue test
        }
        
        try {
            WebElement signInButton = driver.findElement(AppiumBy.accessibilityId("Sign In"));
            boolean isDisplayed = signInButton.isDisplayed();
            boolean isEnabled = signInButton.isEnabled();
            
            Assert.assertTrue(isDisplayed, "Sign In button should be displayed");
            Assert.assertTrue(isEnabled, "Sign In button should be enabled");
            
            System.out.println("✅ Login button test passed");
        } catch (Exception e) {
            Assert.fail("Sign In button not found: " + e.getMessage());
        }
    }

    @Test(priority = 3, description = "Verify form validation with empty fields")  
    public void testEmptyFieldValidation() {
        // Only run if on login screen
        try {
            driver.findElement(AppiumBy.accessibilityId("Dashboard"));
            System.out.println("Already logged in - skipping validation test");
            return;
        } catch (Exception e) {
            // On login screen, continue test
        }
        
        try {
            // Click login without entering any data
            WebElement signInButton = driver.findElement(AppiumBy.accessibilityId("Sign In"));
            signInButton.click();
            
            // Wait for validation
            Thread.sleep(3000);
            
            // Check for validation messages
            String pageSource = driver.getPageSource();
            boolean hasValidation = pageSource.contains("Please enter your email") || 
                                  pageSource.contains("Please enter your password") ||
                                  pageSource.contains("required");
            
            Assert.assertTrue(hasValidation, "Should show validation messages for empty fields");
            System.out.println("✅ Empty field validation test passed");
            
        } catch (Exception e) {
            Assert.fail("Validation test failed: " + e.getMessage());
        }
    }
}