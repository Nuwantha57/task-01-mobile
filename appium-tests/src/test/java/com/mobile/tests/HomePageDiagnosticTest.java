package com.mobile.tests;

import java.io.File;
import java.io.FileWriter;

import org.testng.annotations.Test;

import com.mobile.pages.HomePage;
import com.mobile.pages.LoginPage;
import com.mobile.utils.ConfigReader;

/**
 * Diagnostic test to check what appears on home screen after login
 */
public class HomePageDiagnosticTest extends BaseTest {

    @Test(description = "Diagnostic: Check home screen content after login")
    public void testHomeScreenContent() {
        try {
            System.out.println("=== HOME SCREEN DIAGNOSTIC TEST ===");
            
            // Wait for app to load
            waitFor(3);

            // Login
            LoginPage loginPage = new LoginPage(driver);
            System.out.println("✓ Login page displayed: " + loginPage.isLoginPageDisplayed());
            
            loginPage.login(ConfigReader.getTestEmail(), ConfigReader.getTestPassword());
            System.out.println("✓ Logged in with: " + ConfigReader.getTestEmail());
            
            // Allow time for navigation
            waitFor(5);

            // Check home page
            HomePage homePage = new HomePage(driver);
            System.out.println("✓ Is on home page: " + homePage.isHomePageDisplayed());

            // Get full page source
            String pageSource = driver.getPageSource();
            
            // Save to file for analysis
            File outputFile = new File("home_page_source.xml");
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(pageSource);
            }
            System.out.println("✓ Page source saved to: " + outputFile.getAbsolutePath());

            // Check for various MFA-related text
            System.out.println("\n=== SEARCHING FOR MFA ELEMENTS ===");
            System.out.println("Contains 'Setup MFA': " + pageSource.contains("Setup MFA"));
            System.out.println("Contains 'setup mfa': " + pageSource.contains("setup mfa"));
            System.out.println("Contains 'MFA': " + pageSource.contains("MFA"));
            System.out.println("Contains 'Multi-Factor': " + pageSource.contains("Multi-Factor"));
            System.out.println("Contains 'Two-Factor': " + pageSource.contains("Two-Factor"));
            System.out.println("Contains 'Security': " + pageSource.contains("Security"));
            System.out.println("Contains 'Secure your account': " + pageSource.contains("Secure your account"));
            System.out.println("Contains 'Enable': " + pageSource.contains("Enable"));
            
            // Check for buttons
            System.out.println("\n=== SEARCHING FOR BUTTONS ===");
            int buttonCount = pageSource.split("android.widget.Button").length - 1;
            System.out.println("Number of buttons found: " + buttonCount);
            
            // Extract button content-desc values
            System.out.println("\n=== BUTTON CONTENT-DESC VALUES ===");
            String[] lines = pageSource.split("\n");
            for (String line : lines) {
                if (line.contains("android.widget.Button") && line.contains("content-desc")) {
                    // Extract content-desc value
                    int start = line.indexOf("content-desc=\"");
                    if (start != -1) {
                        start += 14; // length of 'content-desc="'
                        int end = line.indexOf("\"", start);
                        if (end != -1) {
                            String contentDesc = line.substring(start, end);
                            System.out.println("Button: " + contentDesc);
                        }
                    }
                }
            }
            
            // Check if Setup MFA card is visible using HomePage method
            System.out.println("\n=== HOMEPAGE METHOD CHECK ===");
            try {
                boolean setupMfaVisible = homePage.isSetupMfaCardVisible();
                System.out.println("✓ isSetupMfaCardVisible(): " + setupMfaVisible);
            } catch (Exception e) {
                System.out.println("⚠️ isSetupMfaCardVisible() error: " + e.getMessage());
            }

            System.out.println("\n=== DIAGNOSTIC COMPLETE ===");
            System.out.println("Please check the page source file for full details.");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error during diagnostic: " + e.getMessage());
        }
    }
}
