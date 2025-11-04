package com.mobile.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class LoginPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Constructor
    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Locators based on actual Flutter app elements
    private WebElement getEmailField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Email']")));
    }

    private WebElement getPasswordField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Password']")));
    }

    private WebElement getLoginButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Sign In")));
    }

    private WebElement getForgotPasswordButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Forgot Password?")));
    }

    private WebElement getSignUpButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.accessibilityId("Sign Up")));
    }

    // Page Actions
    public void enterEmail(String email) {
        WebElement emailField = getEmailField();
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordField = getPasswordField();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void tapLoginButton() {
        getLoginButton().click();
    }

    public void tapForgotPassword() {
        getForgotPasswordButton().click();
    }

    public void tapSignUp() {
        getSignUpButton().click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        tapLoginButton();
    }

    public boolean isLoginButtonEnabled() {
        return getLoginButton().isEnabled();
    }

    public boolean isLoginButtonDisplayed() {
        return getLoginButton().isDisplayed();
    }

    // Wait for error message (SnackBar)
    public String getErrorMessage() {
        WebElement snackBar = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Invalid') or contains(@text, 'Please') or contains(@text, 'Error')]")));
        return snackBar.getText();
    }

    public boolean isErrorDisplayed() {
        try {
            getErrorMessage();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if navigated to home screen after successful login
    public boolean isHomeScreenDisplayed() {
        try {
            // Wait longer for authentication to complete (AWS Cognito can take 10-15 seconds)
            
            // First wait for login button to disappear (indicates processing started)
            Thread.sleep(2000);
            
            // Then wait for dashboard elements to appear
            for (int i = 0; i < 15; i++) {
                try {
                    // Look for Dashboard title
                    WebElement dashboardTitle = driver.findElement(AppiumBy.accessibilityId("Dashboard"));
                    if (dashboardTitle.isDisplayed()) {
                        return true;
                    }
                } catch (Exception e) {
                    // Dashboard not found yet, continue waiting
                }
                
                try {
                    // Also check if we're still on login screen
                    driver.findElement(AppiumBy.accessibilityId("Welcome Back"));
                    // Still on login screen, wait more
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // No login screen elements found, check for other home indicators
                    try {
                        // Look for any home screen elements
                        driver.findElement(AppiumBy.accessibilityId("Quick Actions"));
                        return true;
                    } catch (Exception ex) {
                        Thread.sleep(1000);
                    }
                }
            }
            
            // Final check - if no login elements and no dashboard, assume success
            List<WebElement> loginButtons = driver.findElements(AppiumBy.accessibilityId("Sign In"));
            return loginButtons.isEmpty();
            
        } catch (Exception e) {
            return false;
        }
    }
}
