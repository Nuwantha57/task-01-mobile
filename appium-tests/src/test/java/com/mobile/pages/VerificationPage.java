package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class VerificationPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public VerificationPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================

    // Verification code field
    private WebElement getVerificationCodeField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Verification Code']")));
    }

    // Verify button
    private WebElement getVerifyButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Verify")));
    }

    // Resend Code button
    private WebElement getResendCodeButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Resend Code")));
    }

    // Page title and elements
    private WebElement getCheckEmailTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Check Your Email")));
    }

    private WebElement getVerifyEmailTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Verify Email")));
    }

    private WebElement getBackButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Back")));
    }

    // Email icon
    private WebElement getEmailIcon() {
        return driver.findElement(AppiumBy.className("android.widget.ImageView"));
    }

    // ==================== PAGE ACTIONS ====================

    // Enter verification code
    public void enterVerificationCode(String code) {
        WebElement codeField = getVerificationCodeField();
        codeField.clear();
        codeField.sendKeys(code);
    }

    // Click Verify button
    public void clickVerify() {
        getVerifyButton().click();
    }

    // Click Resend Code button
    public void clickResendCode() {
        getResendCodeButton().click();
    }

    // Click back button
    public void clickBack() {
        getBackButton().click();
    }

    // ==================== VALIDATION METHODS ====================

    // Check if verification page is displayed
    public boolean isVerificationPageDisplayed() {
        try {
            return getCheckEmailTitle().isDisplayed() || getVerifyEmailTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if Verify button is enabled
    public boolean isVerifyButtonEnabled() {
        try {
            return getVerifyButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if Resend Code button is enabled
    public boolean isResendCodeButtonEnabled() {
        try {
            return getResendCodeButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if error message is displayed
    public boolean isErrorMessageDisplayed(String expectedError) {
        try {
            String pageSource = driver.getPageSource();
            return pageSource.contains(expectedError);
        } catch (Exception e) {
            return false;
        }
    }

    // Check if success message is displayed
    public boolean isSuccessMessageDisplayed(String expectedSuccess) {
        try {
            String pageSource = driver.getPageSource();
            return pageSource.contains(expectedSuccess);
        } catch (Exception e) {
            return false;
        }
    }

    // Get the email address mentioned in the verification message
    public String getDisplayedEmail() {
        try {
            String pageSource = driver.getPageSource();
            // Extract email from "We sent a verification code to..." message
            if (pageSource.contains("We sent a verification code to")) {
                // This is a simplified extraction - in real scenarios you might need regex
                String[] parts = pageSource.split("We sent a verification code to");
                if (parts.length > 1) {
                    String emailPart = parts[1].split("\n")[0].trim();
                    return emailPart;
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    // Check if loading indicator is displayed
    public boolean isLoadingDisplayed() {
        try {
            WebElement loadingIndicator = driver.findElement(AppiumBy.className("android.widget.ProgressBar"));
            return loadingIndicator.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Wait for verification to complete and navigation to occur
    public boolean waitForVerificationSuccess(int timeoutSeconds) {
        try {
            // Wait for success message or navigation away from verification page
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Check if we navigate away from verification page (success)
            customWait.until(driver -> {
                String pageSource = driver.getPageSource();
                return pageSource.contains("Email verified") || 
                       pageSource.contains("Welcome Back") || 
                       pageSource.contains("Sign In") ||
                       !pageSource.contains("Check Your Email");
            });
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get verification code field value
    public String getVerificationCodeValue() {
        try {
            return getVerificationCodeField().getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Clear verification code field
    public void clearVerificationCode() {
        try {
            getVerificationCodeField().clear();
        } catch (Exception e) {
            // Field might not be accessible
        }
    }

    // Check for specific validation messages
    public boolean isInvalidCodeMessageDisplayed() {
        return isErrorMessageDisplayed("Invalid verification code") ||
               isErrorMessageDisplayed("Code mismatch") ||
               isErrorMessageDisplayed("Invalid code");
    }

    public boolean isCodeExpiredMessageDisplayed() {
        return isErrorMessageDisplayed("Code expired") ||
               isErrorMessageDisplayed("Verification code expired");
    }

    public boolean isCodeResentMessageDisplayed() {
        return isSuccessMessageDisplayed("Verification code resent") ||
               isSuccessMessageDisplayed("Code resent");
    }
}