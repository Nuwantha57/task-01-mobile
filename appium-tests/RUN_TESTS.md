# Running Appium MFA Tests - Quick Guide

## Prerequisites (One-time Setup)

1. ✅ Appium installed (v3.1.0) - DONE
2. ✅ Android emulator running - CHECK
3. ✅ APK built - DONE
4. ✅ Maven installed
5. ✅ Test credentials configured in config.properties

## Option 1: Run with PowerShell Script (Easiest)

```powershell
cd appium-tests
.\run-mfa-tests.ps1
```

This will:

- Check prerequisites
- Start Appium server
- Run all MFA tests
- Stop Appium server
- Show results

## Option 2: Manual Step-by-Step

### Step 1: Start Appium Server

```powershell
# In Terminal 1
appium
```

### Step 2: Run Tests

```powershell
# In Terminal 2
cd appium-tests
mvn clean test -Dtest=MfaTest
```

### Step 3: Stop Appium

```
Ctrl+C in Terminal 1
```

## Option 3: Run Specific Test

```powershell
# Start Appium first (Terminal 1)
appium

# Run specific test (Terminal 2)
cd appium-tests
mvn test -Dtest=MfaTest#testValidCodeSetup
```

## Test Flow Executed

The Appium tests will automatically:

1. **testNavigateToMfaSetup**

   - Login with credentials
   - Navigate to Home
   - Click "Setup MFA"
   - Verify MFA Setup screen appears

2. **testQrCodeAndSecretKeyDisplayed**

   - Verify QR code is visible
   - Extract secret key
   - Verify all 3 steps are shown

3. **testMfaSetupWithInvalidCode**

   - Enter "000000"
   - Verify error appears
   - Error handling works

4. **testMfaSetupWithValidCode**

   - Generate TOTP code from secret
   - Enter code
   - Verify success screen
   - Complete setup

5. **testLoginWithMfaValidCode**
   - Logout
   - Login with MFA-enabled account
   - Verify MFA prompt appears
   - Generate TOTP code
   - Enter code
   - Verify login success

## Expected Output

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.mobile.tests.MfaTest
Secret Key (save this for future tests): OWYX4Z2VJCYOIBKVR...
Generated TOTP code: 123456
Generated TOTP code for login: 654321
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Troubleshooting

### "No emulator running"

```powershell
# Check devices
adb devices

# If no device, start emulator
emulator -avd <your_avd_name>
```

### "Appium connection refused"

```powershell
# Make sure Appium is running
appium --allow-insecure chromedriver_autodownload
```

### "Element not found"

- Tests may need to wait longer for UI elements
- Check if app is actually running on emulator
- Verify APK is the latest build

### "TOTP code invalid"

- Code expires every 30 seconds
- Ensure device time is correct
- Regenerate code just before entering

## Viewing Test Results

### Console Output

See test execution in real-time in terminal

### HTML Reports

```
appium-tests/target/surefire-reports/index.html
```

### XML Reports

```
appium-tests/target/surefire-reports/TEST-*.xml
```

## What Gets Tested

✅ Complete MFA setup flow
✅ QR code generation and display
✅ Secret key extraction  
✅ Invalid code error handling
✅ Valid TOTP code verification
✅ Success screen navigation
✅ MFA-enabled login flow
✅ MFA code validation at login

## Success Criteria

All tests pass = MFA implementation is working correctly!

The tests verify the exact flow:

1. Login → Home → Setup MFA → Scan QR → Verify → Success
2. Logout → Login → MFA Prompt → Verify → Home
