package com.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration helper to read test configuration
 */
public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try {
            // Load from classpath (works with Maven)
            InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
            if (input == null) {
                System.err.println("❌ ERROR: Unable to find config.properties in classpath!");
            } else {
                properties.load(input);
                input.close();
                System.out.println("✅ Config loaded successfully. Test email: " + properties.getProperty("test.email"));
            }
        } catch (IOException e) {
            System.err.println("❌ ERROR: Failed to load configuration file: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
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
