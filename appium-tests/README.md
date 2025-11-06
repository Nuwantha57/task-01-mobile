# MFA Appium Test Suite

Automated tests for MFA (Multi-Factor Authentication) functionality in the Staff Auth Mobile app.

## 📋 Prerequisites

### 1. Software Requirements

- **Java JDK 17+** installed
- **Maven 3.6+** installed
- **Node.js** and **npm** installed
- **Appium Server 2.x** installed globally
- **Android Studio** with Android SDK
- **Android Emulator** or physical device

### 2. Appium Setup

```bash
# Install Appium
npm install -g appium

# Install UiAutomator2 driver
appium driver install uiautomator2

# Verify installation
appium -v
```

### 3. Build Flutter App

```bash
# Navigate to project root
cd C:\Intern_Project_Code\task-01-mobile

# Build debug APK
flutter build apk --debug

# APK will be at: build/app/outputs/flutter-apk/app-debug.apk
```

## 🔧 Configuration

### 1. Update config.properties

Edit `src/test/resources/config.properties`:

```properties
# Update APK path
app.path=C:/Intern_Project_Code/task-01-mobile/build/app/outputs/flutter-apk/app-debug.apk

# Update device name (check with: adb devices)
device.name=emulator-5554

# Update platform version
platform.version=13.0

# Test user WITHOUT MFA
test.email=your-test-email@example.com
test.password=YourPassword123!

# Test user WITH MFA already enabled
test.email.with.mfa=your-mfa-user@example.com
test.password.with.mfa=YourPassword123!

# MFA Secret key (get this from first MFA setup)
test.mfa.secret=JBSWY3DPEHPK3PXP
```

### 2. Create Test Users

#### Option A: Create via App

1. Open the app on emulator/device
2. Create two test accounts:
   - One regular account (for testing MFA setup)
   - One account to enable MFA manually (for testing login with MFA)

#### Option B: Create via AWS Cognito Console

1. Go to AWS Cognito → User Pools → Users
2. Create test users
3. Confirm their emails

### 3. Get MFA Secret Key

**IMPORTANT:** For automated testing, you need the MFA secret key.

#### Method 1: From App (Recommended)

1. Run the test `testQrCodeAndSecretKeyDisplayed()` first
2. Check console output for secret key
3. Update `config.properties` with the key

#### Method 2: Manual Setup

1. Open app → Login → Go to Setup MFA
2. Click "Copy secret key" button
3. Paste into `config.properties`

## 🚀 Running Tests

### 1. Start Appium Server

```bash
# Terminal 1: Start Appium
appium

# Should see: [Appium] Appium REST http interface listener started on 0.0.0.0:4723
```

### 2. Start Android Emulator

```bash
# List available AVDs
emulator -list-avds

# Start emulator
emulator -avd Pixel_5_API_33
```

OR start from Android Studio → AVD Manager

### 3. Install Dependencies

```bash
cd C:\Intern_Project_Code\task-01-mobile\appium-tests

# Install Maven dependencies
mvn clean install -DskipTests
```

### 4. Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MfaTest

# Run specific test method
mvn test -Dtest=MfaTest#testNavigateToMfaSetup

# Run with TestNG XML
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## 📊 Test Cases Covered

### ✅ MFA Setup Tests

1. **testNavigateToMfaSetup** - Verify navigation to MFA setup from home
2. **testQrCodeAndSecretKeyDisplayed** - Verify QR code and secret key are displayed
3. **testMfaSetupWithInvalidCode** - Test MFA setup with invalid verification code
4. **testMfaSetupWithValidCode** - Test complete MFA setup with valid TOTP code

### ✅ Login with MFA Tests

5. **testLoginWithMfaValidCode** - Test login with MFA using valid code
6. **testLoginWithMfaInvalidCode** - Test login with invalid MFA code
7. **testLoginWithMfaExpiredCode** - Test login with expired MFA code
8. **testMfaRetryAfterFailures** - Test retry mechanism after failed attempts

### ✅ Validation Tests

9. **testMfaCodeFieldValidation** - Verify MFA code field accepts only 6 digits
10. **testSetupMfaButtonAfterEnabled** - Verify Setup MFA button after enabling

## 📝 Test Execution Order

Tests run in priority order (priority 1-10):

1. Setup Flow
2. Login with MFA
3. Error Scenarios
4. Edge Cases

## 🔍 Viewing Test Results

### Console Output

Test results print to console with:

- Test name and status
- Assertions passed/failed
- Generated TOTP codes (for debugging)
- Secret keys

### TestNG Reports

After tests complete:

```bash
# Open HTML report
start test-output/index.html

# Or navigate to:
# appium-tests/test-output/index.html
```

### Maven Surefire Reports

```bash
# Open Surefire report
start target/surefire-reports/index.html
```

## 🐛 Troubleshooting

### Issue: "Cannot find element"

- **Solution**: Update locators in Page Objects if UI changed
- Check element IDs using Appium Inspector

### Issue: "Secret key is null"

- **Solution**: Ensure you scroll to secret key section
- Verify QR code is displayed before extracting secret

### Issue: "TOTP code invalid"

- **Solution**: Check device time is synced
- Ensure secret key is correct (Base32 format)
- TOTP codes expire every 30 seconds

### Issue: "App not launching"

- **Solution**:
  - Verify APK path in config.properties
  - Ensure app is built: `flutter build apk --debug`
  - Check Appium server is running
  - Verify emulator/device is connected: `adb devices`

### Issue: "Connection refused to Appium"

- **Solution**:
  - Start Appium server: `appium`
  - Check Appium URL in config: `http://localhost:4723`

## 📱 Device Setup

### Android Emulator

```bash
# Create AVD (if not exists)
avdmanager create avd -n Pixel_5_API_33 -k "system-images;android-33;google_apis;x86_64"

# Start emulator
emulator -avd Pixel_5_API_33

# Check devices
adb devices
```

### Physical Device

1. Enable Developer Options on device
2. Enable USB Debugging
3. Connect via USB
4. Trust computer
5. Verify: `adb devices`

## 📚 Test Data Management

### Recommended Approach:

1. Create dedicated test users in Cognito
2. Don't use production accounts
3. Keep credentials in config.properties
4. Never commit secrets to git

### Secret Key Format:

- Must be Base32 encoded
- Example: `JBSWY3DPEHPK3PXP`
- Length: Usually 16-32 characters
- Only uppercase A-Z and numbers 2-7

## 🔐 Security Notes

1. **Don't commit secrets**: Add `config.properties` to `.gitignore`
2. **Use test accounts only**: Never use real user credentials
3. **Rotate secrets**: Change MFA keys after testing
4. **Clean up**: Delete test users after testing

## 📄 Project Structure

```
appium-tests/
├── pom.xml                          # Maven configuration
├── src/
│   └── test/
│       ├── java/com/mobile/
│       │   ├── pages/               # Page Object Models
│       │   │   ├── LoginPage.java
│       │   │   ├── HomePage.java
│       │   │   ├── MfaSetupPage.java
│       │   │   └── MfaConfirmationPage.java
│       │   ├── tests/               # Test Classes
│       │   │   ├── BaseTest.java
│       │   │   └── MfaTest.java
│       │   └── utils/               # Utilities
│       │       ├── TotpGenerator.java
│       │       └── ConfigReader.java
│       └── resources/
│           ├── config.properties    # Configuration
│           └── testng.xml           # TestNG suite
└── target/                          # Build output
```

## ⚙️ Continuous Integration

### GitHub Actions Example

```yaml
name: MFA Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: "17"
      - name: Install Appium
        run: npm install -g appium
      - name: Run tests
        run: cd appium-tests && mvn test
```

## 📞 Support

For issues or questions:

1. Check this README
2. Review test logs in `test-output/`
3. Use Appium Inspector to debug element locators
4. Check MFA Implementation Guide in project root

---

**Last Updated**: November 6, 2025
