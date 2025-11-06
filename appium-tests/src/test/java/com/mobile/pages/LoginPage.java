package com.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for Login Screen
 */
public class LoginPage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================

    private WebElement getWelcomeBackTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//*[contains(@text, 'Welcome Back') or contains(@text, 'Sign In') or contains(@text, 'Login') or contains(@content-desc, 'login')]")
        ));
    }

    private WebElement getEmailField() {
        // Try common patterns for Flutter TextField
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//android.widget.EditText[@hint='Email' or @resource-id='email' or contains(@content-desc,'email')]")
            ));
        } catch (Exception e) {
            // Fallback: first visible EditText
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.className("android.widget.EditText")
            ));
        }
    }

    private WebElement getPasswordField() {
        try {
            return driver.findElement(
                AppiumBy.xpath("//android.widget.EditText[@hint='Password' or contains(@content-desc,'password') or @resource-id='password']")
            );
        } catch (Exception e) {
            // Fallback: the second EditText on screen (email is usually first)
            return driver.findElements(AppiumBy.className("android.widget.EditText")).get(1);
        }
    }

    private WebElement getSignInButton() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@text='Sign In' or @text='Login' or contains(@content-desc,'sign') or contains(@content-desc,'login')]")
            ));
        } catch (Exception e) {
            // Fallback: the first clickable button
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.className("android.widget.Button")
            ));
        }
    }

    private WebElement getForgotPasswordLink() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.TextView[@text='Forgot Password?']"));
    }

    private WebElement getSignUpLink() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Create account')]"));
    }

    private WebElement getTogglePasswordButton() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.Button[contains(@content-desc, 'password')]"));
    }

    // ==================== PAGE ACTIONS ====================

    /**
     * Check if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        try {
            // Any of these elements being visible suggests the login screen
            if (getWelcomeBackTitle() != null) return true;
        } catch (Exception ignored) { }
        try {
            return getSignInButton().isDisplayed();
        } catch (Exception ignored) { }
        try {
            return getEmailField().isDisplayed();
        } catch (Exception ignored) { }
        return false;
    }

    /**
     * Enter email
     */
    public void enterEmail(String email) {
        WebElement emailField = getEmailField();
        emailField.clear();
        emailField.sendKeys(email);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        WebElement passwordField = getPasswordField();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    /**
     * Click sign in button
     */
    public void clickSignIn() {
        driver.hideKeyboard();
        getSignInButton().click();
    }

    /**
     * Perform login
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
        
        // Wait for navigation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Check if sign in button is enabled
     */
    public boolean isSignInButtonEnabled() {
        try {
            return getSignInButton().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPassword() {
        getForgotPasswordLink().click();
    }

    /**
     * Click sign up link
     */
    public void clickSignUp() {
        getSignUpLink().click();
    }

    /**
     * Toggle password visibility
     */
    public void togglePasswordVisibility() {
        getTogglePasswordButton().click();
    }
}
