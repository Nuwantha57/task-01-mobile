package com.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for MFA Confirmation Screen (During Login)
 * Handles MFA code entry when signing in
 */
public class MfaConfirmationPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public MfaConfirmationPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================

    private WebElement getMfaVerificationTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("MFA Verification")));
    }

    private WebElement getEnterMfaCodeTitle() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Enter MFA Code')]"));
    }

    private WebElement getSecurityIcon() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.ImageView[@content-desc='security']"));
    }

    private WebElement getDescriptionText() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, '6-digit code from your authenticator app')]"));
    }

    // MFA code input field
    private WebElement getMfaCodeField() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.EditText[@hint='123456' or contains(@text, 'MFA Code')]")));
    }

    // Verify button
    private WebElement getVerifyButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[@text='Verify']")));
    }

    // Loading indicator
    private WebElement getLoadingIndicator() {
        return driver.findElement(AppiumBy.className("android.widget.ProgressBar"));
    }

    // Error snackbar
    private WebElement getErrorSnackbar() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.TextView[contains(@text, 'failed') or contains(@text, 'Invalid')]"));
    }

    // ==================== PAGE ACTIONS ====================

    /**
     * Check if MFA confirmation page is displayed
     */
    public boolean isMfaConfirmationPageDisplayed() {
        try {
            return getMfaVerificationTitle().isDisplayed() || getEnterMfaCodeTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enter MFA code
     */
    public void enterMfaCode(String code) {
        WebElement codeField = getMfaCodeField();
        codeField.clear();
        codeField.sendKeys(code);
        
        // Hide keyboard
        driver.hideKeyboard();
    }

    /**
     * Click verify button
     */
    public void clickVerify() {
        getVerifyButton().click();
    }

    /**
     * Check if verify button is enabled
     */
    public boolean isVerifyButtonEnabled() {
        try {
            return getVerifyButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if loading indicator is visible
     */
    public boolean isLoading() {
        try {
            return getLoadingIndicator().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if error is displayed
     */
    public boolean isErrorDisplayed() {
        try {
            return getErrorSnackbar().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        try {
            return getErrorSnackbar().getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Submit MFA code and wait for navigation
     */
    public void submitMfaCode(String code) {
        enterMfaCode(code);
        clickVerify();
        
        // Wait for processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verify MFA code field accepts only 6 digits
     */
    public boolean isMfaCodeFieldValid() {
        try {
            WebElement codeField = getMfaCodeField();
            // Check if maxLength is 6
            String maxLength = codeField.getAttribute("maxLength");
            return "6".equals(maxLength);
        } catch (Exception e) {
            return false;
        }
    }
}
