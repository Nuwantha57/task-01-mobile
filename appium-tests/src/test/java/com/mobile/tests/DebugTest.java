package com.mobile.tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class DebugTest extends BaseTest {

    @Test
    public void debugAppElements() {
        System.out.println("=== App launched successfully ===");
        
        // Wait for app to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Print current activity
        System.out.println("Current Activity: " + driver.getCapabilities().getCapability("appium:appActivity"));
        System.out.println("Current Package: " + driver.getCapabilities().getCapability("appium:appPackage"));
        
        // Get page source to see what's available
        System.out.println("Page Source:");
        System.out.println(driver.getPageSource());
        
        // Try to find elements by different strategies
        System.out.println("\n=== Looking for elements ===");
        
        try {
            List<WebElement> allElements = driver.findElements(AppiumBy.xpath("//*"));
            System.out.println("Total elements found: " + allElements.size());
            
            // Look for elements with text or content description
            for (int i = 0; i < Math.min(20, allElements.size()); i++) {
                WebElement element = allElements.get(i);
                String text = element.getText();
                String contentDesc = element.getAttribute("content-desc");
                String className = element.getTagName();
                
                if (!text.isEmpty() || (contentDesc != null && !contentDesc.isEmpty())) {
                    System.out.println("Element " + i + " - Class: " + className + 
                                     ", Text: '" + text + "'" + 
                                     ", ContentDesc: '" + contentDesc + "'");
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding elements: " + e.getMessage());
        }
    }
}