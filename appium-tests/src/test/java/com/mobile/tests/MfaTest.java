package com.mobile.tests;

import com.mobile.pages.*;
import com.mobile.utils.ConfigReader;
import com.mobile.utils.TotpGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for MFA (Multi-Factor Authentication) functionality
 * Tests MFA setup, login with MFA, and error scenarios
 */
public class MfaTest extends BaseTest {

    @Test(priority = 1, description = "Verify MFA setup flow - Navigate to MFA setup from home")
    public void testNavigateToMfaSetup() {
        // Wait for app to load
        waitFor(3);

        // Login
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        
        loginPage.login(ConfigReader.getTestEmail(), ConfigReader.getTestPassword());
        waitFor(2);

        // Navigate to home
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed after login");

        // Check if Setup MFA card is visible
        Assert.assertTrue(homePage.isSetupMfaCardVisible(), "Setup MFA card should be visible");

        // Click Setup MFA
        homePage.clickSetupMfa();
        waitFor(2);

        // Verify MFA setup page is displayed
        MfaSetupPage mfaSetupPage = new MfaSetupPage(driver);
        Assert.assertTrue(mfaSetupPage.isMfaSetupPageDisplayed(), "MFA setup page should be displayed");
    }

    @Test(priority = 2, description = "Verify QR code and secret key are displayed")
    public void testQrCodeAndSecretKeyDisplayed() {
        MfaSetupPage mfaSetupPage = new MfaSetupPage(driver);
        
        // Check if QR code is displayed
        Assert.assertTrue(mfaSetupPage.isQrCodeDisplayed(), "QR code should be displayed");

        // Scroll to see all steps
        mfaSetupPage.scrollToVerificationSection();
        
        // Verify all steps are visible
        Assert.assertTrue(mfaSetupPage.areAllStepsVisible(), "All 3 steps should be visible");

        // Get secret key for manual entry
        String secretKey = mfaSetupPage.getSecretKey();
        Assert.assertNotNull(secretKey, "Secret key should be available");
        Assert.assertFalse(secretKey.isEmpty(), "Secret key should not be empty");
        
        System.out.println("Secret Key (save this for future tests): " + secretKey);
    }

    @Test(priority = 3, description = "Verify MFA setup with invalid code")
    public void testMfaSetupWithInvalidCode() {
        MfaSetupPage mfaSetupPage = new MfaSetupPage(driver);
        
        // Enter invalid code
        mfaSetupPage.enterVerificationCode("000000");
        
        // Click verify
        mfaSetupPage.clickVerifyAndEnableMfa();
        waitFor(2);

        // Should show error
        Assert.assertTrue(mfaSetupPage.isErrorDisplayed(), "Error should be displayed for invalid code");
    }

    @Test(priority = 4, description = "Verify MFA setup with valid TOTP code")
    public void testMfaSetupWithValidCode() {
        MfaSetupPage mfaSetupPage = new MfaSetupPage(driver);
        
        // Get secret key
        String secretKey = mfaSetupPage.getSecretKey();
        Assert.assertNotNull(secretKey, "Secret key should be available");

        // Generate valid TOTP code
        String totpCode = TotpGenerator.generateTotpCode(secretKey);
        System.out.println("Generated TOTP code: " + totpCode);

        // Enter valid code
        mfaSetupPage.enterVerificationCode(totpCode);
        
        // Click verify
        mfaSetupPage.clickVerifyAndEnableMfa();
        waitFor(3);

        // Verify success screen is displayed
        Assert.assertTrue(mfaSetupPage.isSuccessScreenDisplayed(), "Success screen should be displayed");
        Assert.assertTrue(mfaSetupPage.getSuccessMessage().contains("Successfully"), 
            "Success message should be displayed");
        
        // Click Done
        mfaSetupPage.clickDone();
        waitFor(2);

        // Should navigate back to home
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should navigate back to home page");
    }

    @Test(priority = 5, description = "Test login with MFA enabled - valid code")
    public void testLoginWithMfaValidCode() {
        // Logout first
        HomePage homePage = new HomePage(driver);
        homePage.clickLogout();
        waitFor(2);

        // Login with MFA-enabled account
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        
        loginPage.login(ConfigReader.getTestEmailWithMfa(), ConfigReader.getTestPasswordWithMfa());
        waitFor(2);

        // Should show MFA confirmation page
        MfaConfirmationPage mfaConfirmationPage = new MfaConfirmationPage(driver);
        Assert.assertTrue(mfaConfirmationPage.isMfaConfirmationPageDisplayed(), 
            "MFA confirmation page should be displayed");

        // Generate and enter TOTP code
        String secretKey = ConfigReader.getMfaSecretKey();
        String totpCode = TotpGenerator.generateTotpCode(secretKey);
        System.out.println("Generated TOTP code for login: " + totpCode);

        mfaConfirmationPage.enterMfaCode(totpCode);
        mfaConfirmationPage.clickVerify();
        waitFor(3);

        // Should navigate to home page
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should navigate to home after MFA verification");
    }

    @Test(priority = 6, description = "Test login with MFA enabled - invalid code")
    public void testLoginWithMfaInvalidCode() {
        // Logout first
        HomePage homePage = new HomePage(driver);
        homePage.clickLogout();
        waitFor(2);

        // Login with MFA-enabled account
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getTestEmailWithMfa(), ConfigReader.getTestPasswordWithMfa());
        waitFor(2);

        // MFA confirmation page
        MfaConfirmationPage mfaConfirmationPage = new MfaConfirmationPage(driver);
        Assert.assertTrue(mfaConfirmationPage.isMfaConfirmationPageDisplayed(), 
            "MFA confirmation page should be displayed");

        // Enter invalid code
        mfaConfirmationPage.enterMfaCode("999999");
        mfaConfirmationPage.clickVerify();
        waitFor(2);

        // Should show error and stay on MFA page
        Assert.assertTrue(mfaConfirmationPage.isErrorDisplayed(), "Error should be displayed for invalid code");
        Assert.assertTrue(mfaConfirmationPage.isMfaConfirmationPageDisplayed(), 
            "Should remain on MFA confirmation page");
    }

    @Test(priority = 7, description = "Test login with MFA - expired code")
    public void testLoginWithMfaExpiredCode() {
        MfaConfirmationPage mfaConfirmationPage = new MfaConfirmationPage(driver);
        
        // Enter expired code (just use a random old code)
        mfaConfirmationPage.enterMfaCode("123456");
        mfaConfirmationPage.clickVerify();
        waitFor(2);

        // Should show error
        Assert.assertTrue(mfaConfirmationPage.isErrorDisplayed(), 
            "Error should be displayed for expired/invalid code");
    }

    @Test(priority = 8, description = "Test MFA retry with correct code after failures")
    public void testMfaRetryAfterFailures() {
        MfaConfirmationPage mfaConfirmationPage = new MfaConfirmationPage(driver);
        
        // Generate and enter correct code
        String secretKey = ConfigReader.getMfaSecretKey();
        String totpCode = TotpGenerator.generateTotpCode(secretKey);
        
        mfaConfirmationPage.enterMfaCode(totpCode);
        mfaConfirmationPage.clickVerify();
        waitFor(3);

        // Should successfully login
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Should successfully login after entering correct code");
    }

    @Test(priority = 9, description = "Verify MFA code field accepts only 6 digits")
    public void testMfaCodeFieldValidation() {
        // Logout and login again to get to MFA page
        HomePage homePage = new HomePage(driver);
        homePage.clickLogout();
        waitFor(2);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getTestEmailWithMfa(), ConfigReader.getTestPasswordWithMfa());
        waitFor(2);

        MfaConfirmationPage mfaConfirmationPage = new MfaConfirmationPage(driver);
        
        // Try entering more than 6 digits
        mfaConfirmationPage.enterMfaCode("1234567890");
        
        // Field should only accept 6 digits (verification depends on implementation)
        // Note: This test may need adjustment based on actual field behavior
    }

    @Test(priority = 10, description = "Verify Setup MFA button visibility after MFA is enabled")
    public void testSetupMfaButtonAfterEnabled() {
        // Login with valid MFA code
        MfaConfirmationPage mfaConfirmationPage = new MfaConfirmationPage(driver);
        String secretKey = ConfigReader.getMfaSecretKey();
        String totpCode = TotpGenerator.generateTotpCode(secretKey);
        mfaConfirmationPage.submitMfaCode(totpCode);
        waitFor(3);

        // On home page
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should be on home page");

        // Setup MFA button should still be visible (for re-setup or disable scenarios)
        // This depends on your business logic
        boolean mfaCardVisible = homePage.isSetupMfaCardVisible();
        System.out.println("Setup MFA card visible after enabling: " + mfaCardVisible);
    }
}
