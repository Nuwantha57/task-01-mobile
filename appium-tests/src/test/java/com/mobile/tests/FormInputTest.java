package com.mobile.tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class FormInputTest extends BaseTest {

    @Test
    public void testFormInputRetention() {
        System.out.println("=== Testing Form Input Retention ===");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Step 1: Find and interact with email field
        System.out.println("1. Finding email field...");
        WebElement emailField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Email']"));
        System.out.println("✓ Email field found");
        
        System.out.println("2. Clicking email field to focus...");
        emailField.click();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("3. Clearing email field...");
        emailField.clear();
        
        System.out.println("4. Entering email...");
        emailField.sendKeys("nuwanthapiumal57@gmail.com");
        
        System.out.println("5. Checking if email was entered...");
        String emailText = emailField.getText();
        System.out.println("Email field text: '" + emailText + "'");
        
        // Step 2: Find and interact with password field
        System.out.println("6. Finding password field...");
        WebElement passwordField = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@hint='Password']"));
        System.out.println("✓ Password field found");
        
        System.out.println("7. Clicking password field to focus...");
        passwordField.click();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("8. Clearing password field...");
        passwordField.clear();
        
        System.out.println("9. Entering password...");
        passwordField.sendKeys("Nuwantha@1234");
        
        System.out.println("10. Checking if password was entered...");
        String passwordText = passwordField.getText();
        System.out.println("Password field text: '" + passwordText + "' (should be masked)");
        
        // Step 3: Wait a moment and check if values are retained
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("11. Re-checking email field after wait...");
        String emailTextAfter = emailField.getText();
        System.out.println("Email field text after wait: '" + emailTextAfter + "'");
        
        System.out.println("12. Re-checking password field after wait...");
        String passwordTextAfter = passwordField.getText();
        System.out.println("Password field text after wait: '" + passwordTextAfter + "'");
        
        // Step 4: Try to submit and see what happens
        System.out.println("13. Looking for submit button...");
        WebElement signInButton = driver.findElement(AppiumBy.accessibilityId("Sign In"));
        System.out.println("✓ Sign In button found");
        
        System.out.println("14. Clicking Sign In button...");
        signInButton.click();
        
        // Wait for response
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("15. Checking for validation messages...");
        try {
            WebElement emailError = driver.findElement(AppiumBy.accessibilityId("Please enter your email"));
            System.out.println("❌ Email validation error found: " + emailError.getAttribute("content-desc"));
        } catch (Exception e) {
            System.out.println("✓ No email validation error");
        }
        
        try {
            WebElement passwordError = driver.findElement(AppiumBy.accessibilityId("Please enter your password"));
            System.out.println("❌ Password validation error found: " + passwordError.getAttribute("content-desc"));
        } catch (Exception e) {
            System.out.println("✓ No password validation error");
        }
        
        System.out.println("16. Final form state check...");
        System.out.println("Final page source:");
        System.out.println(driver.getPageSource());
    }
}