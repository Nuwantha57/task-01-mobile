package com.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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

    private WebElement getSetupMfaTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Setup MFA")));
    }

    private WebElement getSecurityIcon() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.ImageView[@content-desc='security']"));
    }

    private WebElement getEnableTwoFactorTitle() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Enable Two-Factor Authentication')]"));
    }

    private WebElement getStep1Title() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Install an Authenticator App')]"));
    }

    private WebElement getStep2Title() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Scan QR Code')]"));
    }

    private WebElement getStep3Title() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Enter Verification Code')]"));
    }

    // QR Code image view
    private WebElement getQrCodeImage() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.className("android.widget.ImageView")));
    }

    // Secret key text for manual entry
    private WebElement getSecretKeyText() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Can\\'t scan?')]/..//android.widget.EditText"));
    }

    // Copy secret key button
    private WebElement getCopySecretKeyButton() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.Button[@content-desc='Copy secret key']"));
    }

    // Verification code input field
    private WebElement getVerificationCodeField() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.EditText[@hint='123456' or @text='Verification Code']")));
    }

    // Verify and Enable MFA button
    private WebElement getVerifyButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[contains(@text, 'Verify and Enable MFA')]")));
    }

    // Success screen elements
    private WebElement getSuccessIcon() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.ImageView[@content-desc='check_circle']")));
    }

    private WebElement getSuccessTitle() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'MFA Successfully Enabled')]"));
    }

    private WebElement getDoneButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[@text='Done']")));
    }

    // Back button
    private WebElement getBackButton() {
        return driver.findElement(AppiumBy.accessibilityId("Back"));
    }

    // ==================== PAGE ACTIONS ====================

    /**
     * Check if MFA Setup page is displayed
     */
    public boolean isMfaSetupPageDisplayed() {
        try {
            return getSetupMfaTitle().isDisplayed() || getEnableTwoFactorTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if QR code is displayed
     */
    public boolean isQrCodeDisplayed() {
        try {
            return getQrCodeImage().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scroll to verification code section (Step 3)
     */
    public void scrollToVerificationSection() {
        // Scroll down to see Step 3 and verification field
        driver.findElement(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true))" +
            ".scrollIntoView(new UiSelector().textContains(\"Enter Verification Code\"))"
        ));
    }

    /**
     * Get secret key for manual entry (if visible)
     */
    public String getSecretKey() {
        try {
            scrollToSecretKeySection();
            return getSecretKeyText().getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Scroll to secret key section
     */
    private void scrollToSecretKeySection() {
        driver.findElement(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true))" +
            ".scrollIntoView(new UiSelector().textContains(\"Can't scan?\"))"
        ));
    }

    /**
     * Click copy secret key button
     */
    public void clickCopySecretKey() {
        scrollToSecretKeySection();
        getCopySecretKeyButton().click();
    }

    /**
     * Enter verification code
     */
    public void enterVerificationCode(String code) {
        scrollToVerificationSection();
        WebElement codeField = getVerificationCodeField();
        codeField.clear();
        codeField.sendKeys(code);
        
        // Hide keyboard
        driver.hideKeyboard();
    }

    /**
     * Click Verify and Enable MFA button
     */
    public void clickVerifyAndEnableMfa() {
        scrollToVerificationSection();
        getVerifyButton().click();
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
            return getSuccessIcon().isDisplayed() && getSuccessTitle().isDisplayed();
        } catch (Exception e) {
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
