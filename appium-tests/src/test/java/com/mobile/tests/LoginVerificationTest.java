package com.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mobile.pages.HomePage;
import com.mobile.pages.LoginPage;

/**
 * Simple login verification test
 */
public class LoginVerificationTest extends BaseTest {

    @Test(priority = 1, description = "Verify user can login successfully")
    public void testLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        // Get credentials
        String email = com.mobile.utils.ConfigReader.getTestEmail();
        String password = com.mobile.utils.ConfigReader.getTestPassword();
        
        System.out.println("Attempting login with: " + email);
        
        // Perform login
        loginPage.login(email, password);
        
        System.out.println("✓ Login clicked, waiting for result...");
        Thread.sleep(5000); // Wait for login to process
        
        // Print page source to debug
        String pageSource = driver.getPageSource();
        System.out.println("\n========== CURRENT PAGE STATE ==========");
        
        // Check for error messages
        if (pageSource.contains("Invalid credentials") || pageSource.contains("Incorrect") || pageSource.contains("failed")) {
            System.out.println("❌ ERROR: Login failed - invalid credentials");
            System.out.println("Please verify:");
            System.out.println("1. Account exists: zpiumal@gmail.com");
            System.out.println("2. Password is correct: Piumal@1234");
            System.out.println("3. Account is verified/confirmed");
        }
        
        // Check for MFA screen
        if (pageSource.contains("MFA") || pageSource.contains("verification code") || pageSource.contains("authenticator")) {
            System.out.println("⚠️  MFA screen detected - account has MFA enabled");
            System.out.println("This account cannot be used for MFA setup tests");
        }
        
        // Check for home screen
        if (pageSource.contains("Dashboard") || pageSource.contains("Quick Actions")) {
            System.out.println("✓ SUCCESS: Login successful, on home screen");
            boolean isHome = homePage.isHomePageDisplayed();
            System.out.println("✓ HomePage.isHomePageDisplayed() = " + isHome);
        } else {
            System.out.println("❌ Still on login screen - login did not succeed");
            System.out.println("\nVisible text elements:");
            // Extract visible text for debugging
            if (pageSource.contains("Welcome Back")) {
                System.out.println("- Welcome Back (login screen)");
            }
            if (pageSource.contains("Sign in to continue")) {
                System.out.println("- Sign in to continue");
            }
        }
        
        // Verify home page is displayed
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Login should navigate to home page. Check console output for details.");
    }
}
