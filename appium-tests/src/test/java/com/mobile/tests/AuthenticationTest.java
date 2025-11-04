package com.mobile.tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class AuthenticationTest extends BaseTest {

    @Test
    public void testCompleteAuthenticationFlow() {
        System.out.println("=== Testing Complete Authentication Flow ===");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Step 1: Enter credentials properly
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
        
        // Step 3: Wait for authentication response (longer timeout)
        System.out.println("3. Waiting for authentication response...");
        
        try {
            // Wait for either success (navigation away from login) or error
            Thread.sleep(15000); // Wait 15 seconds for network request
            
            System.out.println("4. Checking authentication result...");
            
            // Check if we're still on login screen or moved to home
            try {
                driver.findElement(AppiumBy.accessibilityId("Welcome Back"));
                System.out.println("❌ Still on login screen - authentication may have failed");
                
                // Check for any error messages
                String pageSource = driver.getPageSource();
                if (pageSource.contains("Invalid") || pageSource.contains("Error") || pageSource.contains("Failed")) {
                    System.out.println("❌ Error detected in page source");
                } else {
                    System.out.println("⚠️ No obvious error - may be network delay");
                }
                
            } catch (Exception e) {
                System.out.println("✅ No longer on login screen - authentication likely successful!");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Exception during wait: " + e.getMessage());
        }
        
        // Step 4: Final status check
        System.out.println("5. Final status check...");
        String finalPageSource = driver.getPageSource();
        
        // Look for home screen indicators
        if (finalPageSource.contains("Welcome Back") && finalPageSource.contains("Sign in to continue")) {
            System.out.println("📱 Status: Still on login screen");
            
            // Check if button is still disabled (processing)
            try {
                WebElement button = driver.findElement(AppiumBy.accessibilityId("Sign In"));
                boolean isEnabled = button.isEnabled();
                System.out.println("Sign In button enabled: " + isEnabled);
                if (!isEnabled) {
                    System.out.println("⏳ Login still processing...");
                }
            } catch (Exception e) {
                System.out.println("❓ Could not check button state");
            }
            
        } else {
            System.out.println("🎉 Status: Successfully navigated away from login screen!");
        }
        
        System.out.println("6. Complete page source:");
        System.out.println(finalPageSource);
    }
}