package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ResetPasswordPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public ResetPasswordPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================

    // Verification code field
    private WebElement getVerificationCodeField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Verification Code']")));
    }

    // New password field
    private WebElement getNewPasswordField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='New Password']")));
    }

    // Confirm password field
    private WebElement getConfirmPasswordField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Confirm New Password']")));
    }

    // Reset Password button
    private WebElement getResetPasswordButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Reset Password")));
    }

    // Password visibility toggles
    private WebElement getNewPasswordVisibilityToggle() {
        return driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='New Password']/following-sibling::android.widget.Button"));
    }

    private WebElement getConfirmPasswordVisibilityToggle() {
        return driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Confirm New Password']/following-sibling::android.widget.Button"));
    }

    // Page title and elements
    private WebElement getCreateNewPasswordTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Create New Password")));
    }

    private WebElement getResetPasswordTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Reset Password")));
    }

    private WebElement getBackButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Back")));
    }

    // ==================== PAGE ACTIONS ====================

    // Enter verification code
    public void enterVerificationCode(String code) {
        WebElement codeField = getVerificationCodeField();
        codeField.clear();
        codeField.sendKeys(code);
    }

    // Enter new password
    public void enterNewPassword(String password) {
        WebElement passwordField = getNewPasswordField();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    // Enter confirm password
    public void enterConfirmPassword(String confirmPassword) {
        WebElement confirmPasswordField = getConfirmPasswordField();
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    // Click Reset Password button
    public void clickResetPassword() {
        getResetPasswordButton().click();
    }

    // Toggle new password visibility
    public void toggleNewPasswordVisibility() {
        getNewPasswordVisibilityToggle().click();
    }

    // Toggle confirm password visibility
    public void toggleConfirmPasswordVisibility() {
        getConfirmPasswordVisibilityToggle().click();
    }

    // Click back button
    public void clickBack() {
        getBackButton().click();
    }

    // Fill complete reset password form
    public void fillResetPasswordForm(String verificationCode, String newPassword, String confirmPassword) {
        enterVerificationCode(verificationCode);
        enterNewPassword(newPassword);
        enterConfirmPassword(confirmPassword);
    }

    // ==================== VALIDATION METHODS ====================

    // Check if reset password page is displayed
    public boolean isResetPasswordPageDisplayed() {
        try {
            return getCreateNewPasswordTitle().isDisplayed() || getResetPasswordTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if Reset Password button is enabled
    public boolean isResetPasswordButtonEnabled() {
        try {
            return getResetPasswordButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Get field values for validation
    public String getVerificationCodeValue() {
        try {
            return getVerificationCodeField().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getNewPasswordValue() {
        try {
            return getNewPasswordField().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getConfirmPasswordValue() {
        try {
            return getConfirmPasswordField().getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Clear fields
    public void clearVerificationCode() {
        try {
            getVerificationCodeField().clear();
        } catch (Exception e) {
            // Field might not be accessible
        }
    }

    public void clearNewPassword() {
        try {
            getNewPasswordField().clear();
        } catch (Exception e) {
            // Field might not be accessible
        }
    }

    public void clearConfirmPassword() {
        try {
            getConfirmPasswordField().clear();
        } catch (Exception e) {
            // Field might not be accessible
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

    // Check for specific validation messages
    public boolean isInvalidCodeMessageDisplayed() {
        return isErrorMessageDisplayed("Invalid verification code") ||
               isErrorMessageDisplayed("Code mismatch") ||
               isErrorMessageDisplayed("Invalid code");
    }

    public boolean isPasswordMismatchMessageDisplayed() {
        return isErrorMessageDisplayed("Passwords do not match") ||
               isErrorMessageDisplayed("password mismatch") ||
               isErrorMessageDisplayed("match");
    }

    public boolean isWeakPasswordMessageDisplayed() {
        return isErrorMessageDisplayed("Password must be at least 8 characters") ||
               isErrorMessageDisplayed("8 characters") ||
               isErrorMessageDisplayed("weak password");
    }

    public boolean isEmptyFieldMessageDisplayed() {
        return isErrorMessageDisplayed("Please enter") ||
               isErrorMessageDisplayed("required") ||
               isErrorMessageDisplayed("Enter");
    }

    public boolean isPasswordResetSuccessMessageDisplayed() {
        return isSuccessMessageDisplayed("Password reset successful") ||
               isSuccessMessageDisplayed("Password has been reset") ||
               isSuccessMessageDisplayed("reset successful");
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

    // Wait for password reset completion and navigation
    public boolean waitForPasswordResetSuccess(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Wait for success message or navigation to login screen
            customWait.until(driver -> {
                String pageSource = driver.getPageSource();
                return pageSource.contains("Password reset successful") ||
                       pageSource.contains("Welcome Back") ||
                       pageSource.contains("Sign In") ||
                       !pageSource.contains("Create New Password");
            });
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if all required fields are accessible
    public boolean areAllFieldsAccessible() {
        try {
            return getVerificationCodeField().isDisplayed() &&
                   getNewPasswordField().isDisplayed() &&
                   getConfirmPasswordField().isDisplayed() &&
                   getResetPasswordButton().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Validate page content
    public boolean hasExpectedPageContent() {
        try {
            String pageSource = driver.getPageSource();
            return pageSource.contains("Create New Password") &&
                   pageSource.contains("Verification Code") &&
                   pageSource.contains("New Password") &&
                   pageSource.contains("Confirm New Password");
        } catch (Exception e) {
            return false;
        }
    }

    // Get email mentioned in the page (from description text)
    public String getEmailFromDescription() {
        try {
            String pageSource = driver.getPageSource();
            // Extract email from "Enter the code sent to..." message
            if (pageSource.contains("Enter the code sent to")) {
                String[] parts = pageSource.split("Enter the code sent to");
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
}