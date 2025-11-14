package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

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
            // Flutter buttons use content-desc, not text attribute
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@content-desc='Sign In' or @content-desc='Login' or contains(@content-desc,'sign')]")
            ));
        } catch (Exception e) {
            // Fallback: find by text if content-desc doesn't work
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[@text='Sign In' or @text='Login']")
                ));
            } catch (Exception e2) {
                // Last resort: the last button on screen (usually the submit button)
                java.util.List<WebElement> buttons = driver.findElements(AppiumBy.className("android.widget.Button"));
                return buttons.get(buttons.size() - 1);
            }
        }
    }

    private WebElement getForgotPasswordLink() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.Button[@content-desc='Forgot Password?' or contains(@content-desc,'Forgot')]"));
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
        emailField.click();  // Ensure field is focused
        emailField.sendKeys(email);
        
        // Verify text was entered
        try {
            Thread.sleep(500);
            String enteredText = emailField.getText();
            if (enteredText == null || enteredText.isEmpty()) {
                System.out.println("WARNING: Email field appears empty after sendKeys");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        WebElement passwordField = getPasswordField();
        passwordField.clear();
        passwordField.click();  // Ensure field is focused
        passwordField.sendKeys(password);
        
        // Verify text was entered
        try {
            Thread.sleep(500);
            String enteredText = passwordField.getText();
            if (enteredText == null || enteredText.isEmpty()) {
                System.out.println("WARNING: Password field appears empty after sendKeys");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Click sign in button
     */
    public void clickSignIn() {
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            // Keyboard might not be showing, ignore
        }
        
        WebElement signInButton = getSignInButton();
        System.out.println("Clicking Sign In button...");
        signInButton.click();
    }

    /**
     * Perform login
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
        
        // Wait for navigation - AWS Cognito authentication can take time
        try {
            Thread.sleep(5000);  // Increased from 2000 to 5000ms
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
