package com.mobile.tests;

import org.testng.annotations.Test;

public class LoginFlowTest extends BaseTest {

    @Test
    public void testLoginFlow() {
        System.out.println("=== Testing Login Flow ===");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("1. Looking for email field...");
        try {
            driver.findElement(io.appium.java_client.AppiumBy.xpath("//android.widget.EditText[@hint='Email']")).sendKeys("nuwanthapiumal57@gmail.com");
            System.out.println("✓ Email entered successfully");
        } catch (Exception e) {
            System.out.println("✗ Failed to enter email: " + e.getMessage());
        }
        
        System.out.println("2. Looking for password field...");
        try {
            driver.findElement(io.appium.java_client.AppiumBy.xpath("//android.widget.EditText[@hint='Password']")).sendKeys("Nuwantha@1234");
            System.out.println("✓ Password entered successfully");
        } catch (Exception e) {
            System.out.println("✗ Failed to enter password: " + e.getMessage());
        }
        
        System.out.println("3. Clicking login button...");
        try {
            driver.findElement(io.appium.java_client.AppiumBy.accessibilityId("Sign In")).click();
            System.out.println("✓ Login button clicked successfully");
        } catch (Exception e) {
            System.out.println("✗ Failed to click login: " + e.getMessage());
        }
        
        System.out.println("4. Waiting for response...");
        try {
            Thread.sleep(10000); // Wait 10 seconds for login attempt
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("5. Checking current screen...");
        System.out.println("Page Source after login attempt:");
        System.out.println(driver.getPageSource());
    }
}