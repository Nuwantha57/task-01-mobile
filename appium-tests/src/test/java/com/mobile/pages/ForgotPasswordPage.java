package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ForgotPasswordPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public ForgotPasswordPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================

    // Email field for password reset
    private WebElement getEmailField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Email']")));
    }

    // Send Code button
    private WebElement getSendCodeButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Send Code")));
    }

    // Page title and elements
    private WebElement getResetPasswordTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Reset Password")));
    }

    private WebElement getForgotPasswordTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Forgot Password")));
    }

    private WebElement getBackButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Back")));
    }

    // Lock reset icon
    private WebElement getLockResetIcon() {
        return driver.findElement(AppiumBy.className("android.widget.ImageView"));
    }

    // Description text
    private WebElement getDescriptionText() {
        return driver.findElement(AppiumBy.xpath("//*[contains(@text, 'verification code')]"));
    }

    // ==================== PAGE ACTIONS ====================

    // Enter email for password reset
    public void enterEmail(String email) {
        WebElement emailField = getEmailField();
        emailField.clear();
        emailField.sendKeys(email);
    }

    // Click Send Code button
    public void clickSendCode() {
        getSendCodeButton().click();
    }

    // Click back button
    public void clickBack() {
        getBackButton().click();
    }

    // ==================== VALIDATION METHODS ====================

    // Check if forgot password page is displayed
    public boolean isForgotPasswordPageDisplayed() {
        try {
            return getResetPasswordTitle().isDisplayed() || getForgotPasswordTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if Send Code button is enabled
    public boolean isSendCodeButtonEnabled() {
        try {
            return getSendCodeButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if email field is accessible
    public boolean isEmailFieldAccessible() {
        try {
            WebElement emailField = getEmailField();
            return emailField.isDisplayed() && emailField.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Get email field value
    public String getEmailValue() {
        try {
            return getEmailField().getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Clear email field
    public void clearEmail() {
        try {
            getEmailField().clear();
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
    public boolean isInvalidEmailMessageDisplayed() {
        return isErrorMessageDisplayed("Please enter a valid email") ||
               isErrorMessageDisplayed("valid email") ||
               isErrorMessageDisplayed("Invalid email");
    }

    public boolean isEmptyEmailMessageDisplayed() {
        return isErrorMessageDisplayed("Please enter your email") ||
               isErrorMessageDisplayed("email required") ||
               isErrorMessageDisplayed("Enter your email");
    }

    public boolean isCodeSentMessageDisplayed() {
        return isSuccessMessageDisplayed("Verification code sent") ||
               isSuccessMessageDisplayed("Code sent") ||
               isSuccessMessageDisplayed("sent to");
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

    // Navigate to forgot password from login screen
    public void navigateFromLogin() {
        try {
            // Click "Forgot Password?" link on login screen
            WebElement forgotPasswordLink = driver.findElement(AppiumBy.accessibilityId("Forgot Password?"));
            forgotPasswordLink.click();
            
            // Wait for forgot password page to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId("Reset Password")));
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to forgot password screen: " + e.getMessage());
        }
    }

    // Wait for navigation to reset password screen
    public boolean waitForNavigationToResetScreen(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Wait for navigation to reset password screen (look for reset password specific elements)
            customWait.until(driver -> {
                String pageSource = driver.getPageSource();
                return pageSource.contains("Create New Password") ||
                       pageSource.contains("Verification Code") ||
                       pageSource.contains("New Password") ||
                       !pageSource.contains("Send Code");
            });
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check for specific error scenarios
    public boolean isUserNotFoundErrorDisplayed() {
        return isErrorMessageDisplayed("User does not exist") ||
               isErrorMessageDisplayed("Username/client id combination not found") ||
               isErrorMessageDisplayed("User not found");
    }

    public boolean isNetworkErrorDisplayed() {
        return isErrorMessageDisplayed("Network error") ||
               isErrorMessageDisplayed("Connection failed") ||
               isErrorMessageDisplayed("network");
    }

    // Validate page content
    public boolean hasExpectedPageContent() {
        try {
            String pageSource = driver.getPageSource();
            return pageSource.contains("Reset Password") &&
                   pageSource.contains("verification code") &&
                   pageSource.contains("Email");
        } catch (Exception e) {
            return false;
        }
    }
}