package com.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumBy;

public class SmartLoginTest extends BaseTest {
    
    @BeforeMethod
    public void pageSetup() {
        // Add initial wait for app to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, description = "Smart login test - handles both login and already logged in scenarios")
    public void testSmartLogin() {
        System.out.println("=== Running Smart Login Test ===");
        
        // First check if we're already logged in (from previous test)
        System.out.println("1. Checking current app state...");
        
        boolean alreadyLoggedIn = false;
        try {
            // Look for home screen indicators first
            driver.findElement(AppiumBy.accessibilityId("Dashboard"));
            System.out.println("✅ Already on Dashboard - user is logged in!");
            alreadyLoggedIn = true;
        } catch (Exception e) {
            try {
                driver.findElement(AppiumBy.accessibilityId("Quick Actions"));
                System.out.println("✅ Already on home screen - user is logged in!");
                alreadyLoggedIn = true;
            } catch (Exception e2) {
                // Check for login screen
                try {
                    driver.findElement(AppiumBy.accessibilityId("Welcome Back"));
                    System.out.println("📱 On login screen - need to authenticate");
                    alreadyLoggedIn = false;
                } catch (Exception e3) {
                    System.out.println("❓ Unknown screen state - checking page source");
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains("Dashboard") || pageSource.contains("Quick Actions")) {
                        System.out.println("✅ Home screen detected in page source");
                        alreadyLoggedIn = true;
                    } else if (pageSource.contains("Welcome Back") || pageSource.contains("Sign In")) {
                        System.out.println("📱 Login screen detected in page source");
                        alreadyLoggedIn = false;
                    }
                }
            }
        }
        
        if (alreadyLoggedIn) {
            System.out.println("🎉 User is already authenticated - test passed!");
        } else {
            System.out.println("2. Need to login - entering credentials...");
            
            // Find and fill email field
            WebElement emailField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Email']"));
            emailField.click();
            emailField.clear();
            emailField.sendKeys("nuwanthapiumal57@gmail.com");
            
            // Find and fill password field
            WebElement passwordField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Password']"));
            passwordField.click();
            passwordField.clear();
            passwordField.sendKeys("Nuwantha@1234");
            
            System.out.println("✓ Credentials entered");
            
            // Submit the form
            System.out.println("3. Submitting login form...");
            WebElement signInButton = driver.findElement(AppiumBy.accessibilityId("Sign In"));
            signInButton.click();
            System.out.println("✓ Sign In button clicked");
            
            // Wait for authentication
            System.out.println("4. Waiting for authentication...");
            try {
                Thread.sleep(15000); // Wait for authentication
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Check result
            System.out.println("5. Checking authentication result...");
            try {
                driver.findElement(AppiumBy.accessibilityId("Welcome Back"));
                System.out.println("❌ Still on login screen - authentication failed");
                alreadyLoggedIn = false;
            } catch (Exception e) {
                System.out.println("✅ No longer on login screen - authentication successful!");
                alreadyLoggedIn = true;
            }
        }
        
        // Final verification
        System.out.println("6. Final verification...");
        boolean isOnHomeScreen = false;
        
        // Check for multiple home screen indicators
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
                // Check page source for user email or dashboard content
                String pageSource = driver.getPageSource();
                if (pageSource.contains("nuwanthapiumal57@gmail.com") || 
                    pageSource.contains("Dashboard") ||
                    pageSource.contains("Quick Actions")) {
                    isOnHomeScreen = true;
                    System.out.println("✅ Home screen content found in page source");
                }
            }
        }
        
        System.out.println("7. Final result: isOnHomeScreen = " + isOnHomeScreen);
        
        // Assertion
        Assert.assertTrue(isOnHomeScreen, 
            "User should be on home screen (either already logged in or after successful login)");
        
        System.out.println("🎉 SMART LOGIN TEST PASSED!");
    }
}