package com.mobile.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration helper to read test configuration
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE = "src/test/resources/config.properties";

    static {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load configuration file: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getAppPath() {
        return getProperty("app.path");
    }

    public static String getAppiumUrl() {
        return getProperty("appium.url");
    }

    public static String getTestEmail() {
        return getProperty("test.email");
    }

    public static String getTestPassword() {
        return getProperty("test.password");
    }

    public static String getTestEmailWithMfa() {
        return getProperty("test.email.with.mfa");
    }

    public static String getTestPasswordWithMfa() {
        return getProperty("test.password.with.mfa");
    }

    public static String getMfaSecretKey() {
        return getProperty("test.mfa.secret");
    }

    public static String getDeviceName() {
        return getProperty("device.name");
    }

    public static String getPlatformVersion() {
        return getProperty("platform.version");
    }
}
