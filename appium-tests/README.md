# Appium Mobile Automation Tests

## Project Structure

```
appium-tests/
├── pom.xml
├── src/test/
│   ├── java/com/mobile/
│   │   ├── pages/
│   │   │   └── LoginPage.java
│   │   └── tests/
│   │       ├── BaseTest.java
│   │       └── LoginTest.java
│   └── resources/
│       └── testng.xml
```

## Prerequisites

1. Java JDK 11 or higher
2. Maven 3.6+
3. Appium Server 2.x
4. Android SDK
5. Android Emulator or Real Device

## Setup Instructions

### 1. Install Appium

```powershell
npm install -g appium
appium driver install uiautomator2
```

### 2. Start Appium Server

```powershell
appium
```

### 3. Start Android Emulator

```powershell
# List available emulators
emulator -list-avds

# Start the emulator
emulator -avd "Medium Phone API 36.1"
```

### 4. Build the Flutter App (Debug APK)

```powershell
cd C:\Intern_Project_Code\task-01-mobile
flutter build apk --debug
```

### 5. Install Maven Dependencies

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests
mvn clean install
```

## Running Tests

### Run All Tests

```powershell
mvn test
```

### Run Specific Test Class

```powershell
mvn test -Dtest=LoginTest
```

### Run Specific Test Method

```powershell
mvn test -Dtest=LoginTest#testValidLogin
```

### Run with TestNG XML

```powershell
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## Test Coverage

### Login Tests

- ✅ Valid login with correct credentials
- ✅ Invalid login with wrong credentials
- ✅ Empty email field validation
- ✅ Empty password field validation
- ✅ Both fields empty validation
- ✅ Invalid email format validation
- ✅ Navigate to Forgot Password screen
- ✅ Navigate to Sign Up screen
- ✅ Login button state verification

## Widget Keys Added to Flutter App

The following keys were added to `lib/screens/login_screen.dart`:

- `emailField` - Email input field
- `passwordField` - Password input field
- `loginButton` - Sign In button
- `forgotPasswordButton` - Forgot Password link
- `signUpButton` - Sign Up link

## Configuration

- **Emulator:** Medium Phone API 36.1
- **APK Path:** C:/Intern_Project_Code/task-01-mobile/build/app/outputs/apk/debug/app-debug.apk
- **Appium Server:** http://127.0.0.1:4723
- **Automation Engine:** UiAutomator2
- **Timeout:** 15 seconds (explicit waits)

## Notes

- All tests use **explicit waits** (WebDriverWait) - no Thread.sleep
- Page Object Model (POM) pattern implemented
- Tests use **Accessibility IDs** from Flutter widget keys
- Error messages are captured from SnackBar widgets
- Tests are independent and can run in any order
