package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class RegistrationPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public RegistrationPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================
    
    // Username field
    private WebElement getUsernameField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Username']")));
    }

    // Full Name field  
    private WebElement getFullNameField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Full Name']")));
    }

    // Email field
    private WebElement getEmailField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Email']")));
    }

    // Phone Number field
    private WebElement getPhoneField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Phone Number']")));
    }

    // Password field
    private WebElement getPasswordField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Password']")));
    }

    // Confirm Password field
    private WebElement getConfirmPasswordField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Confirm Password']")));
    }

    // Sign Up button
    private WebElement getSignUpButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Sign Up")));
    }

    // Password visibility toggles
    private WebElement getPasswordVisibilityToggle() {
        // Find the visibility icon next to password field
        return driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Password']/following-sibling::android.widget.Button"));
    }

    private WebElement getConfirmPasswordVisibilityToggle() {
        // Find the visibility icon next to confirm password field
        return driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Confirm Password']/following-sibling::android.widget.Button"));
    }

    // Page title and elements
    private WebElement getCreateAccountTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.accessibilityId("Create Account")));
    }

    private WebElement getBackButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Back")));
    }

    // ==================== PAGE ACTIONS ====================

    // Enter Username
    public void enterUsername(String username) {
        WebElement usernameField = getUsernameField();
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    // Enter Full Name
    public void enterFullName(String fullName) {
        WebElement nameField = getFullNameField();
        nameField.clear();
        nameField.sendKeys(fullName);
    }

    // Enter Email
    public void enterEmail(String email) {
        WebElement emailField = getEmailField();
        emailField.clear();
        emailField.sendKeys(email);
    }

    // Enter Phone Number
    public void enterPhoneNumber(String phoneNumber) {
        WebElement phoneField = getPhoneField();
        phoneField.clear();
        phoneField.sendKeys(phoneNumber);
    }

    // Enter Password
    public void enterPassword(String password) {
        WebElement passwordField = getPasswordField();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    // Enter Confirm Password
    public void enterConfirmPassword(String confirmPassword) {
        WebElement confirmPasswordField = getConfirmPasswordField();
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    // Click Sign Up button
    public void clickSignUp() {
        getSignUpButton().click();
    }

    // Toggle password visibility
    public void togglePasswordVisibility() {
        getPasswordVisibilityToggle().click();
    }

    // Toggle confirm password visibility
    public void toggleConfirmPasswordVisibility() {
        getConfirmPasswordVisibilityToggle().click();
    }

    // Click back button
    public void clickBack() {
        getBackButton().click();
    }

    // ==================== VALIDATION METHODS ====================

    // Check if registration page is displayed
    public boolean isRegistrationPageDisplayed() {
        try {
            return getCreateAccountTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if Sign Up button is enabled
    public boolean isSignUpButtonEnabled() {
        try {
            return getSignUpButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Get validation error message for a specific field
    public String getFieldValidationError(String fieldName) {
        try {
            // Look for validation text near the field
            WebElement errorElement = driver.findElement(
                AppiumBy.xpath("//android.widget.EditText[@hint='" + fieldName + "']/following-sibling::android.widget.TextView"));
            return errorElement.getText();
        } catch (Exception e) {
            return "";
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

    // Fill all registration fields
    public void fillRegistrationForm(String username, String fullName, String email, 
                                   String phoneNumber, String password, String confirmPassword) {
        enterUsername(username);
        enterFullName(fullName);
        enterEmail(email);
        enterPhoneNumber(phoneNumber);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
    }

    // Get current field values for validation
    public String getUsernameValue() {
        return getUsernameField().getText();
    }

    public String getEmailValue() {
        return getEmailField().getText();
    }

    public String getPhoneValue() {
        return getPhoneField().getText();
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

    // Navigate to registration screen from login
    public void navigateToRegistrationFromLogin() {
        try {
            // Click Sign Up button on login screen
            WebElement signUpFromLogin = driver.findElement(AppiumBy.accessibilityId("Sign Up"));
            signUpFromLogin.click();
            
            // Wait for registration page to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId("Create Account")));
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to registration screen: " + e.getMessage());
        }
    }
}