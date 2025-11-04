# 🎯 Test Execution Guide

## Quick Test Commands

### Run All Login Tests

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests
mvn test
```

### Run Specific Test

```powershell
# Valid login test only
mvn test -Dtest=LoginTest#testValidLogin

# Invalid login test
mvn test -Dtest=LoginTest#testInvalidLogin

# Email validation test
mvn test -Dtest=LoginTest#testInvalidEmailFormat
```

### Using PowerShell Script

```powershell
# Run all login tests
.\run-tests.ps1

# Run specific test
.\run-tests.ps1 -TestMethod testValidLogin
```

### Using Batch File

```cmd
run-tests.bat
```

## Test Case Details

### 1. testValidLogin

**Purpose**: Verify successful login with valid credentials  
**Steps**:

1. Enter valid email
2. Enter valid password
3. Tap login button
4. Verify home screen is displayed

**Update Required**: Change email/password in test or config.properties

### 2. testInvalidLogin

**Purpose**: Verify error for wrong credentials  
**Steps**:

1. Enter invalid email
2. Enter wrong password
3. Tap login button
4. Verify error message contains "invalid" or "incorrect"

### 3. testEmptyEmail

**Purpose**: Validate empty email field  
**Steps**:

1. Leave email empty
2. Enter password
3. Tap login button
4. Verify error is displayed

### 4. testEmptyPassword

**Purpose**: Validate empty password field  
**Steps**:

1. Enter email
2. Leave password empty
3. Tap login button
4. Verify error is displayed

### 5. testEmptyFields

**Purpose**: Validate both fields empty  
**Steps**:

1. Leave both fields empty
2. Tap login button
3. Verify error is displayed

### 6. testInvalidEmailFormat

**Purpose**: Validate email format  
**Steps**:

1. Enter invalid email format ("notanemail")
2. Enter valid password
3. Tap login button
4. Verify error is displayed

### 7. testForgotPasswordNavigation

**Purpose**: Verify forgot password link works  
**Steps**:

1. Tap "Forgot Password?" link
2. Verify navigation to forgot password screen

### 8. testSignUpNavigation

**Purpose**: Verify sign up link works  
**Steps**:

1. Tap "Sign Up" link
2. Verify navigation to sign up screen

### 9. testLoginButtonState

**Purpose**: Verify login button is displayed and enabled  
**Steps**:

1. Check login button is displayed
2. Check login button is enabled

## Customizing Tests

### Update Test Credentials

**Option 1: Edit config.properties**

```properties
# src/test/resources/config.properties
test.valid.email=your-test-user@example.com
test.valid.password=YourPassword123!
```

**Option 2: Edit LoginTest.java**

```java
// Line 21-22 in LoginTest.java
loginPage.enterEmail("your-test-user@example.com");
loginPage.enterPassword("YourPassword123!");
```

### Adjust Timeouts

**BaseTest.java**

```java
// Line 21
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
```

**LoginPage.java**

```java
// Line 15
this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
```

### Change Device/APK

**BaseTest.java** (Lines 16-18)

```java
options.setDeviceName("Your Emulator Name");
options.setApp("C:/path/to/your/app.apk");
```

## Reading Test Reports

### Console Output

Real-time output shows:

- Test name
- Pass/Fail status
- Error details if failed

### Surefire HTML Report

Location: `target/surefire-reports/index.html`

- Open in browser
- Shows all test results
- Execution time
- Error stack traces

### TestNG Report

Location: `test-output/index.html`

- Detailed test results
- Suite summary
- Test groups
- Execution timeline

## Debugging Failed Tests

### Common Issues

**1. Element Not Found**

- Check widget keys in Flutter code
- Verify APK was rebuilt with keys
- Increase wait timeout

**2. Timeout Exception**

- Emulator is slow - increase timeouts
- Check if app launched correctly
- Verify Appium server is responsive

**3. Invalid Credentials Error**

- Update test credentials
- Check Cognito user exists
- Verify user is confirmed in Cognito

**4. Appium Connection Failed**

- Ensure Appium running on port 4723
- Check URL: http://127.0.0.1:4723
- Restart Appium server

**5. Device Not Found**

- Start emulator before running tests
- Check device name matches exactly
- Run `adb devices` to verify

## Adding More Tests

### Create New Test Method

```java
@Test(priority = 10, description = "Your test description")
public void testYourNewTest() {
    // Your test steps
    loginPage.enterEmail("test@example.com");
    loginPage.enterPassword("password");
    loginPage.tapLoginButton();

    // Your assertions
    Assert.assertTrue(condition, "Error message");
}
```

### Create New Page Object

When I automate your next feature (Sign Up, Forgot Password, etc.):

1. I'll create a new Page class in `src/test/java/com/mobile/pages/`
2. I'll create a new Test class in `src/test/java/com/mobile/tests/`
3. I'll update `testng.xml` to include the new test

## Best Practices

✅ **Always rebuild APK** after changing Flutter widget keys  
✅ **Start Appium first** before running tests  
✅ **Ensure emulator is ready** before test execution  
✅ **Update test credentials** with real test users  
✅ **Run tests individually** when debugging  
✅ **Check reports** for detailed failure information  
✅ **Keep timeouts reasonable** (not too short, not too long)

## Performance Tips

🚀 **Faster test execution**:

- Use `options.setNoReset(true)` to skip app reinstall (after first run)
- Run tests in parallel (configure TestNG)
- Use faster emulator (x86_64 images)

🚀 **Better stability**:

- Use explicit waits for all interactions
- Add proper error handling
- Take screenshots on failure (can be added)

## Next Steps

When ready to automate more features, just tell me:

- Feature name (e.g., "Sign Up", "Forgot Password", "MFA")
- I'll generate the Page Object + Tests immediately

Your test framework is production-ready! 🎉
