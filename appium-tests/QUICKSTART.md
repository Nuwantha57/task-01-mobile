# Quick Start Guide - Appium Mobile Automation

## 🚀 Quick Setup (5 Steps)

### Step 1: Install Appium

```powershell
npm install -g appium
appium driver install uiautomator2
```

### Step 2: Start Appium Server (Terminal 1)

```powershell
appium
```

Keep this terminal open.

### Step 3: Start Android Emulator (Terminal 2)

```powershell
# Navigate to Android SDK
cd C:\Users\YourUsername\AppData\Local\Android\Sdk\emulator

# Start emulator
emulator -avd "Medium Phone API 36.1"
```

Wait for emulator to fully start.

### Step 4: Build Flutter App (Terminal 3)

```powershell
cd C:\Intern_Project_Code\task-01-mobile
flutter build apk --debug
```

### Step 5: Run Tests

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests
mvn clean test
```

## 📋 What Was Created

### Flutter App Changes

✅ Added test keys to `lib/screens/login_screen.dart`:

- `emailField`
- `passwordField`
- `loginButton`
- `forgotPasswordButton`
- `signUpButton`

✅ Added test key to `lib/screens/home_screen.dart`:

- `homeScreen`

### Test Automation Files

✅ **appium-tests/pom.xml** - Maven configuration with dependencies
✅ **src/test/java/com/mobile/tests/BaseTest.java** - Appium driver setup
✅ **src/test/java/com/mobile/pages/LoginPage.java** - Page Object Model
✅ **src/test/java/com/mobile/tests/LoginTest.java** - 9 test cases
✅ **src/test/resources/testng.xml** - TestNG suite configuration

## 🧪 Test Cases Included

1. ✅ Valid login with correct credentials
2. ✅ Invalid login with wrong credentials
3. ✅ Empty email field validation
4. ✅ Empty password field validation
5. ✅ Both fields empty validation
6. ✅ Invalid email format validation
7. ✅ Navigate to Forgot Password screen
8. ✅ Navigate to Sign Up screen
9. ✅ Login button state verification

## 🎯 Running Specific Tests

```powershell
# Run all login tests
mvn test -Dtest=LoginTest

# Run specific test
mvn test -Dtest=LoginTest#testValidLogin
mvn test -Dtest=LoginTest#testInvalidLogin

# Run with TestNG XML
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## 📊 View Test Results

After running tests:

- Console output shows pass/fail
- HTML report: `appium-tests/target/surefire-reports/index.html`
- TestNG report: `appium-tests/test-output/index.html`

## ⚠️ Important Notes

- **Valid credentials needed**: Update `testValidLogin()` with real test user credentials
- **Emulator must match**: "Medium Phone API 36.1"
- **APK path is absolute**: C:/Intern_Project_Code/task-01-mobile/build/app/outputs/apk/debug/app-debug.apk
- **No Thread.sleep**: All waits are explicit (WebDriverWait)
- **Appium URL**: http://127.0.0.1:4723

## 🔧 Troubleshooting

**Issue**: Cannot find dependencies

```powershell
cd appium-tests
mvn clean install -U
```

**Issue**: Appium connection failed

- Check Appium is running on port 4723
- Verify emulator is started

**Issue**: Element not found

- Rebuild Flutter app with test keys
- Check widget keys match in Flutter code

**Issue**: Tests timing out

- Increase wait times in BaseTest/LoginPage
- Check emulator performance

## 📝 Next Steps

When you want to test other features, tell me:

- **Feature name**: (e.g., "Sign Up", "Forgot Password", "MFA")
- I'll generate Page Object + Test class for that feature

Ready to test! 🎉
