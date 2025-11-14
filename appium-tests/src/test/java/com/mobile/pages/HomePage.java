package com.mobile.pages;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

/**
 * Page Object for Home/Dashboard Screen
 */
public class HomePage {
    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==================== LOCATORS ====================

    private WebElement getDashboardTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.view.View[@content-desc='Dashboard']")
        ));
    }

    @SuppressWarnings("unused")
    private WebElement getQuickActionsTitle() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Quick Actions')]"));
    }

    // Setup MFA action card - Button with content-desc
    private WebElement getSetupMfaCard() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.widget.Button[contains(@content-desc, 'Setup MFA')]")));
    }

    // Edit Profile card
    private WebElement getEditProfileCard() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Edit Profile')]"));
    }

    // User Info card
    private WebElement getUserInfoCard() {
        return driver.findElement(
            AppiumBy.xpath("//android.view.View[contains(@content-desc, 'User Info')]"));
    }

    // Logout button - IconButton in AppBar (ImageButton)
    private WebElement getLogoutButton() {
        try {
            // Try finding by description or resource-id first
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.ImageButton[@content-desc='Logout' or contains(@content-desc,'logout')]")
            ));
        } catch (Exception e1) {
            try {
                // Flutter's IconButton typically shows as android.widget.ImageButton
                // The logout icon should be the last ImageButton in the AppBar
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("(//android.widget.ImageButton)[last()]")
                ));
            } catch (Exception e2) {
                // Fallback: Find ImageButton near Dashboard View
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.view.View[@content-desc='Dashboard']" +
                                 "/ancestor::android.view.ViewGroup//android.widget.ImageButton[last()]")
                ));
            }
        }
    }

    // Refresh button
    private WebElement getRefreshButton() {
        return driver.findElement(AppiumBy.accessibilityId("refresh"));
    }

    // User display name
    private WebElement getUserDisplayName() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='displayName']"));
    }

    // User email
    private WebElement getUserEmail() {
        return driver.findElement(
            AppiumBy.xpath("//android.widget.TextView[@resource-id='email']"));
    }

    // ==================== PAGE ACTIONS ====================

    /**
     * Check if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        try {
            return getDashboardTitle().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click Setup MFA action card
     */
    public void clickSetupMfa() {
        // Scroll to Setup MFA button if needed
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().descriptionContains(\"Setup MFA\"))"
            ));
        } catch (Exception e) {
            // Button already visible
        }
        
        // Click the button
        getSetupMfaCard().click();
    }

    /**
     * Click Edit Profile card
     */
    public void clickEditProfile() {
        getEditProfileCard().click();
    }

    /**
     * Click User Info card
     */
    public void clickUserInfo() {
        getUserInfoCard().click();
    }

    /**
     * Click logout button
     */
    public void clickLogout() {
        getLogoutButton().click();
    }

    /**
     * Click refresh button
     */
    public void clickRefresh() {
        getRefreshButton().click();
    }

    /**
     * Get user display name from card
     */
    public String getUserDisplayNameText() {
        try {
            return getUserDisplayName().getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get user email from card
     */
    public String getUserEmailText() {
        try {
            return getUserEmail().getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if Setup MFA card is visible
     */
    public boolean isSetupMfaCardVisible() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().descriptionContains(\"Setup MFA\"))"
            ));
            return getSetupMfaCard().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for home page to load
     */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//*[@content-desc='Dashboard' or contains(@text,'Dashboard')]")
        ));
    }
}
