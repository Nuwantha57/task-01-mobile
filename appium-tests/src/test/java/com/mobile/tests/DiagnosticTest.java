package com.mobile.tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.mobile.pages.LoginPage;
import com.mobile.utils.ConfigReader;

import io.appium.java_client.AppiumBy;

/**
 * Diagnostic test to capture actual UI elements on screen
 */
public class DiagnosticTest extends BaseTest {

    @Test(priority = 1)
    public void captureAllScreenElements() throws Exception {
        System.out.println("\n========== LOGIN SCREEN ELEMENTS ==========");
        Thread.sleep(3000);
        
        // Print login screen
        System.out.println("\n=== LOGIN SCREEN PAGE SOURCE ===");
        System.out.println(driver.getPageSource());
        
        // Login
        System.out.println("\n========== LOGGING IN ==========");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getTestEmail(), ConfigReader.getTestPassword());
        
        Thread.sleep(8000);  // Wait for navigation
        
        System.out.println("\n========== HOME SCREEN ELEMENTS ==========");
        System.out.println("\n=== HOME SCREEN PAGE SOURCE ===");
        System.out.println(driver.getPageSource());
        
        // Try to click Setup MFA
        try {
            System.out.println("\n========== CLICKING SETUP MFA ==========");
            // Setup MFA is a Button with content-desc containing "Setup MFA"
            WebElement setupMfaButton = driver.findElement(
                AppiumBy.xpath("//android.widget.Button[contains(@content-desc, 'Setup MFA')]")
            );
            setupMfaButton.click();
            Thread.sleep(5000);
            
            System.out.println("\n========== MFA SETUP SCREEN ELEMENTS ==========");
            System.out.println("\n=== MFA SETUP PAGE SOURCE ===");
            System.out.println(driver.getPageSource());
        } catch (Exception e) {
            System.out.println("Error navigating to MFA setup: " + e.getMessage());
        }
    }
}
