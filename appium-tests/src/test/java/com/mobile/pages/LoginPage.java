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
            AppiumBy.xpath("//*[contains(@text, 'Welcome Back')]")));
    }

    private WebElement getEmailField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.widget.EditText[@hint='Email']")));
    }

    private WebElement getPasswordField() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.EditText[@hint='Password']"));
    }

    private WebElement getSignInButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[@text='Sign In']")));
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
            return getWelcomeBackTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
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
