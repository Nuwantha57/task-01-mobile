package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

/**
 * Page Object for MFA Setup Screen
 * Handles TOTP setup with QR code scanning and verification
 */
public class MfaSetupPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public MfaSetupPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ==================== LOCATORS ====================

    // AppBar title - Back button indicates we're on the right screen
    private WebElement getBackButton() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.Button[@content-desc='Back']")));
    }

    // Security icon at top of page (currently unused but kept for future use)
    @SuppressWarnings("unused")
    private WebElement getSecurityIcon() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[@content-desc='security' or contains(@content-desc, 'Icons.security')]"));
    }

    // Main title "Enable Two-Factor Authentication"
    private WebElement getEnableTwoFactorTitle() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[@content-desc='Enable Two-Factor Authentication']"));
    }

    // Step titles - using content-desc
    private WebElement getStep1Title() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Install an Authenticator App')]"));
    }

    private WebElement getStep2Title() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[@content-desc='Scan QR Code']"));
    }

    private WebElement getStep3Title() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Enter Verification Code')]"));
    }

    // QR Code - Look for View with content-desc "qr code" (currently unused but kept for future use)
    @SuppressWarnings("unused")
    private WebElement getQrCodeImage() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.xpath("//android.view.View[@content-desc='qr code']")));
    }

    // Copy secret key button (currently unused but kept for future use)
    @SuppressWarnings("unused")
    private WebElement getCopySecretKeyButton() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.Button[@content-desc='Copy secret key']"));
    }

    // Secret key text - Find the View element that contains the secret key
    private String getSecretKeyFromScreen() {
        try {
            // The secret key is in a View element with hint="Can't scan? Enter manually:"
            // and the text attribute contains the actual secret
            WebElement secretElement = driver.findElement(
                AppiumBy.xpath("//android.view.View[@hint=\"Can't scan? Enter manually:\" and string-length(@text) >= 16]"));
            return secretElement.getAttribute("text");
        } catch (Exception e) {
            System.out.println("Error getting secret key: " + e.getMessage());
            return null;
        }
    }

    // Verification code input field
    private WebElement getVerificationCodeField() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.EditText[@hint='Verification Code']")));
    }

    // Verify and Enable MFA button
    private WebElement getVerifyButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[@content-desc='Verify and Enable MFA']")));
    }

    // Success screen elements (currently unused but kept for future use)
    @SuppressWarnings("unused")
    private WebElement getSuccessIcon() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.view.View[@content-desc='check_circle' or contains(@content-desc, 'Icons.check_circle') or contains(@content-desc, 'success') or contains(@content-desc, 'Success')]")));
    }

    private WebElement getSuccessTitle() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[contains(@content-desc, 'MFA Successfully Enabled') or contains(@content-desc, 'Successfully') or contains(@content-desc, 'success')]"));
    }

    private WebElement getDoneButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[@content-desc='Done' or contains(@content-desc, 'Done')]")));
    }

    // ==================== PAGE ACTIONS ====================

    /**
     * Check if MFA Setup page is displayed
     */
    public boolean isMfaSetupPageDisplayed() {
        try {
            return getBackButton().isDisplayed() || getEnableTwoFactorTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if QR code is displayed
     */
    public boolean isQrCodeDisplayed() {
        try {
            // Try to find the QR code element with a fresh wait
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement qrCode = shortWait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.view.View[@content-desc='qr code']")));
            return qrCode.isDisplayed();
        } catch (Exception e) {
            System.out.println("QR code not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Scroll to verification code section (Step 3)
     */
    public void scrollToVerificationSection() {
        try {
            // Scroll down to see verification field
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().descriptionContains(\"Verify and Enable MFA\"))"
            ));
        } catch (Exception e) {
            // Element may already be visible
        }
    }

    /**
     * Get secret key for manual entry (if visible)
     */
    public String getSecretKey() {
        try {
            scrollToSecretKeySection();
            // Try the helper method first
            String secret = getSecretKeyFromScreen();
            if (secret != null && !secret.isEmpty()) {
                return secret.trim();
            }
            // If helper method failed, return null
            return null;
        } catch (Exception e) {
            System.err.println("Error getting secret key: " + e.getMessage());
            return null;
        }
    }

    /**
     * Scroll to secret key section
     */
    private void scrollToSecretKeySection() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().descriptionContains(\"Copy secret key\"))"
            ));
        } catch (Exception e) {
            // Element may already be visible
        }
    }

    /**
     * Enter verification code
     */
    public void enterVerificationCode(String code) {
        scrollToVerificationSection();
        WebElement codeField = getVerificationCodeField();
        codeField.click(); // Focus the field first
        codeField.clear();
        codeField.sendKeys(code);
        
        // Hide keyboard with error handling
        try {
            Thread.sleep(500); // Wait for keyboard
            driver.hideKeyboard();
            Thread.sleep(500); // Wait for keyboard to hide
        } catch (Exception e) {
            // Keyboard may already be hidden
        }
    }

    /**
     * Click Verify and Enable MFA button
     */
    public void clickVerifyAndEnableMfa() {
        try {
            // Scroll to make sure button is visible
            scrollToVerificationSection();
            Thread.sleep(1000); // Wait for scroll to complete
            
            WebElement verifyButton = getVerifyButton();
            
            // Try clicking multiple times if needed
            for (int i = 0; i < 3; i++) {
                try {
                    verifyButton.click();
                    System.out.println("✓ Clicked Verify button (attempt " + (i + 1) + ")");
                    
                    // Wait for response with a separate sleep outside the loop
                    Thread.sleep(2000); 
                    
                    // Check if still on same screen
                    String pageSource = driver.getPageSource();
                    if (!pageSource.contains("Verification Code")) {
                        // Successfully moved away from verification screen
                        System.out.println("✓ Verification submitted successfully");
                        return;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during verification", e);
                }
            }
            
            // If we're still here, try one more time with tap
            System.out.println("⚠️ Standard click didn't work, trying tap...");
            verifyButton.click();
            Thread.sleep(3000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while clicking verify button", e);
        } catch (Exception e) {
            System.err.println("Error clicking verify button: " + e.getMessage());
            throw new RuntimeException("Failed to click verify button", e);
        }
    }

    /**
     * Check if verification button is enabled
     */
    public boolean isVerifyButtonEnabled() {
        try {
            scrollToVerificationSection();
            return getVerifyButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if success screen is displayed
     */
    public boolean isSuccessScreenDisplayed() {
        try {
            // Give it some time to load
            Thread.sleep(3000);
            
            // Check page source for success indicators
            String pageSource = driver.getPageSource();
            System.out.println("=== Checking for success screen ===");
            
            // Debug: Print indicators
            System.out.println("Page contains 'Successfully': " + pageSource.contains("Successfully"));
            System.out.println("Page contains 'success': " + pageSource.contains("success"));
            System.out.println("Page contains 'enabled': " + pageSource.contains("enabled"));
            System.out.println("Page contains 'MFA'': " + pageSource.contains("MFA"));
            System.out.println("Page contains 'Done': " + pageSource.contains("Done"));
            System.out.println("Page contains 'Verification Code': " + pageSource.contains("Verification Code"));
            
            // Check for success indicators
            boolean hasSuccess = pageSource.contains("Successfully") || 
                               pageSource.contains("MFA Successfully Enabled") ||
                               pageSource.contains("success") ||
                               (pageSource.contains("enabled") && pageSource.contains("MFA"));
            
            // Check if we left the verification screen
            boolean leftVerificationScreen = !pageSource.contains("Verification Code") && 
                                             !pageSource.contains("Enter the 6-digit code");
            
            System.out.println("hasSuccess: " + hasSuccess);
            System.out.println("leftVerificationScreen: " + leftVerificationScreen);
            
            if (hasSuccess || (leftVerificationScreen && pageSource.contains("Done"))) {
                System.out.println("✓ Success screen detected!");
                return true;
            }
            
            // Try to find success elements
            try {
                boolean titleDisplayed = getSuccessTitle().isDisplayed();
                System.out.println("Success title displayed: " + titleDisplayed);
                return titleDisplayed;
            } catch (Exception e) {
                System.out.println("Could not find success title element");
                // If we left verification screen and there's a Done button, likely successful
                return leftVerificationScreen && pageSource.contains("Done");
            }
        } catch (Exception e) {
            System.out.println("Error checking success screen: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        try {
            return getSuccessTitle().getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click Done button on success screen
     */
    public void clickDone() {
        getDoneButton().click();
    }

    /**
     * Click back button
     */
    public void clickBack() {
        getBackButton().click();
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorDisplayed() {
        try {
            WebElement errorSnackbar = driver.findElement(
                AppiumBy.xpath("//android.widget.TextView[contains(@text, 'failed') or contains(@text, 'Invalid')]"));
            return errorSnackbar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        try {
            WebElement errorSnackbar = driver.findElement(
                AppiumBy.xpath("//android.widget.TextView[contains(@text, 'failed') or contains(@text, 'Invalid')]"));
            return errorSnackbar.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Complete MFA setup with verification code
     */
    public boolean completeMfaSetup(String verificationCode) {
        try {
            enterVerificationCode(verificationCode);
            clickVerifyAndEnableMfa();
            
            // Wait for success screen
            Thread.sleep(2000);
            
            return isSuccessScreenDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify all steps are visible
     */
    public boolean areAllStepsVisible() {
        try {
            scrollToVerificationSection();
            return getStep1Title().isDisplayed() && 
                   getStep2Title().isDisplayed() && 
                   getStep3Title().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
