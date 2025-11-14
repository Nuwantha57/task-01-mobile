package com.mobile.tests;

import org.testng.annotations.Test;

import com.mobile.pages.HomePage;
import com.mobile.pages.LoginPage;
import com.mobile.pages.MfaSetupPage;
import com.mobile.utils.TotpGenerator;

/**
 * Diagnostic test for MFA verification flow
 */
public class MfaVerificationDiagnosticTest extends BaseTest {

    @Test(description = "Debug MFA verification flow")
    public void testMfaVerificationFlow() throws InterruptedException {
        // Login
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        
        String email = com.mobile.utils.ConfigReader.getTestEmail();
        String password = com.mobile.utils.ConfigReader.getTestPassword();
        
        loginPage.login(email, password);
        Thread.sleep(3000);
        
        // Navigate to MFA setup
        homePage.clickSetupMfa();
        Thread.sleep(3000);
        
        MfaSetupPage mfaSetupPage = new MfaSetupPage(driver);
        
        // Get secret key
        String secretKey = mfaSetupPage.getSecretKey();
        System.out.println("\n✓ Secret Key: " + secretKey);
        
        // Generate TOTP code
        String totpCode = TotpGenerator.generateTotpCode(secretKey);
        System.out.println("✓ Generated TOTP: " + totpCode);
        
        // Scroll to verification section
        System.out.println("\n=== Scrolling to verification section ===");
        mfaSetupPage.scrollToVerificationSection();
        Thread.sleep(2000);
        
        // Print page source before entering code
        System.out.println("\n=== BEFORE entering code ===");
        String beforeSource = driver.getPageSource();
        if (beforeSource.contains("Verification Code")) {
            System.out.println("✓ Verification Code field found");
        }
        if (beforeSource.contains("Verify and Enable MFA")) {
            System.out.println("✓ Verify button found in page source");
        }
        
        // Enter code
        System.out.println("\n=== Entering verification code: " + totpCode + " ===");
        mfaSetupPage.enterVerificationCode(totpCode);
        Thread.sleep(2000);
        
        // Print page source after entering code
        System.out.println("\n=== AFTER entering code ===");
        String afterSource = driver.getPageSource();
        
        // Check if code was entered
        if (afterSource.contains(totpCode)) {
            System.out.println("✓ Code appears in page source: " + totpCode);
        } else {
            System.out.println("❌ Code NOT found in page source");
        }
        
        // Look for verify button
        System.out.println("\n=== Looking for Verify button ===");
        try {
            // Print all buttons on screen
            System.out.println("Buttons on screen:");
            if (afterSource.contains("content-desc=\"Verify and Enable MFA\"")) {
                System.out.println("✓ Found: Verify and Enable MFA button");
            }
            if (afterSource.contains("content-desc=\"Verify\"")) {
                System.out.println("✓ Found: Verify button");
            }
            
            // Extract button info
            int buttonStart = afterSource.indexOf("android.widget.Button");
            while (buttonStart != -1) {
                int buttonEnd = afterSource.indexOf("/>", buttonStart);
                if (buttonEnd != -1) {
                    String buttonInfo = afterSource.substring(buttonStart, buttonEnd);
                    if (buttonInfo.contains("content-desc")) {
                        int descStart = buttonInfo.indexOf("content-desc=\"") + 14;
                        int descEnd = buttonInfo.indexOf("\"", descStart);
                        if (descEnd > descStart) {
                            String desc = buttonInfo.substring(descStart, descEnd);
                            System.out.println("  - Button: " + desc);
                        }
                    }
                }
                buttonStart = afterSource.indexOf("android.widget.Button", buttonEnd);
            }
        } catch (Exception e) {
            System.out.println("Error analyzing buttons: " + e.getMessage());
        }
        
        // Try to click verify button
        System.out.println("\n=== Attempting to click Verify button ===");
        try {
            mfaSetupPage.clickVerifyAndEnableMfa();
            System.out.println("✓ Clicked Verify button successfully");
            Thread.sleep(3000);
            
            // Check result
            System.out.println("\n=== Checking result after click ===");
            String resultSource = driver.getPageSource();
            
            if (resultSource.contains("Successfully") || resultSource.contains("success")) {
                System.out.println("✓ SUCCESS message found!");
            } else if (resultSource.contains("Invalid") || resultSource.contains("incorrect")) {
                System.out.println("❌ ERROR message - invalid code");
            } else if (resultSource.contains("Verification Code")) {
                System.out.println("⚠️  Still on verification screen - button click may not have worked");
            } else {
                System.out.println("? Unknown state");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("❌ Test interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error clicking verify button: " + e.getMessage());
        }
        
        System.out.println("\n=== Test complete ===");
    }
}
