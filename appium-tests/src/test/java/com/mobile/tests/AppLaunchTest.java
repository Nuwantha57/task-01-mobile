package com.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Minimal smoke test to validate emulator connectivity, Appium session, and app launch.
 */
public class AppLaunchTest extends BaseTest {

    @Test(description = "Verify app launches and renders a UI hierarchy")
    public void testAppLaunches() {
        // Give the app a moment to render the first frame
        waitFor(3);

        String source = driver.getPageSource();
        Assert.assertNotNull(source, "Page source should not be null");
        Assert.assertTrue(source.contains("android.widget"),
                "Page source should contain Android widgets, got: " + source.substring(0, Math.min(200, source.length())));
    }
}
