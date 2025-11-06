package com.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
            AppiumBy.xpath("//*[@content-desc='Dashboard' or contains(@text,'Dashboard') or contains(@content-desc,'dashboard')]")
        ));
    }

    private WebElement getQuickActionsTitle() {
        return driver.findElement(
            AppiumBy.xpath("//*[contains(@text, 'Quick Actions')]"));
    }

    // Setup MFA action card
    private WebElement getSetupMfaCard() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Setup MFA') or " +
                          ".//android.widget.TextView[@text='Setup MFA']]")));
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

    // Logout button
    private WebElement getLogoutButton() {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("logout")
            ));
        } catch (Exception e1) {
            try {
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//*[@content-desc='Logout' or @text='Logout' or contains(@text,'Log out')]")
                ));
            } catch (Exception e2) {
                // Fallback: any button that looks like logout by text
                return wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.xpath("//android.widget.Button[contains(@text,'Logout') or contains(@text,'Log out')]")
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
        // Scroll to Setup MFA card if needed
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().textContains(\"Setup MFA\"))"
            ));
        } catch (Exception e) {
            // Card already visible
        }
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
                ".scrollIntoView(new UiSelector().textContains(\"Setup MFA\"))"
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
