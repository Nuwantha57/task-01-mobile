package com.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mobile.pages.HomePage;
import com.mobile.pages.LoginPage;
import com.mobile.pages.MfaSetupPage;
import com.mobile.utils.ConfigReader;

/**
 * Diagnostic test to check MFA setup screen navigation
 */
public class MfaSetupNavigationTest extends BaseTest {

    @Test(description = "Diagnostic: Navigate to MFA setup screen and check elements")
    public void testNavigateToMfaSetupScreen() {
        try {
            System.out.println("=== MFA SETUP NAVIGATION TEST ===");
            
            // Wait for app to load
            waitFor(3);

            // Login
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(ConfigReader.getTestEmail(), ConfigReader.getTestPassword());
            System.out.println("✓ Logged in");
            
            waitFor(5);

            // Navigate to Setup MFA
            HomePage homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isHomePageDisplayed(), "Should be on home page");
            System.out.println("✓ On home page");
            
            Assert.assertTrue(homePage.isSetupMfaCardVisible(), "Setup MFA card should be visible");
            System.out.println("✓ Setup MFA card is visible");
            
            // Click Setup MFA
            homePage.clickSetupMfa();
            System.out.println("✓ Clicked Setup MFA button");
            
            // Wait for navigation
            waitFor(5);
            
            // Check page source for MFA setup elements
            String pageSource = driver.getPageSource();
            System.out.println("\n=== CHECKING MFA SETUP SCREEN ===");
            System.out.println("Contains 'QR': " + pageSource.contains("QR"));
            System.out.println("Contains 'qr code': " + pageSource.contains("qr code"));
            System.out.println("Contains 'Scan': " + pageSource.contains("Scan"));
            System.out.println("Contains 'Verification Code': " + pageSource.contains("Verification Code"));
            System.out.println("Contains 'Enter the 6-digit': " + pageSource.contains("Enter the 6-digit"));
            System.out.println("Contains 'Verify': " + pageSource.contains("Verify"));
            System.out.println("Contains 'Enable': " + pageSource.contains("Enable"));
            System.out.println("Contains 'authenticator': " + pageSource.contains("authenticator"));
            System.out.println("Contains 'Can't scan': " + pageSource.contains("Can't scan"));
            
            // Try to use MfaSetupPage methods
            MfaSetupPage mfaSetupPage = new MfaSetupPage(driver);
            
            System.out.println("\n=== TRYING MfaSetupPage METHODS ===");
            try {
                boolean qrVisible = mfaSetupPage.isQrCodeDisplayed();
                System.out.println("✓ QR code visible: " + qrVisible);
            } catch (Exception e) {
                System.out.println("⚠️ QR code check failed: " + e.getMessage());
            }
            
            try {
                String secretKey = mfaSetupPage.getSecretKey();
                if (secretKey != null) {
                    System.out.println("✓ Secret key found: " + secretKey.substring(0, Math.min(10, secretKey.length())) + "...");
                } else {
                    System.out.println("⚠️ Secret key is null");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Secret key extraction failed: " + e.getMessage());
            }
            
            System.out.println("\n=== TEST COMPLETE ===");
            
        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }
}
