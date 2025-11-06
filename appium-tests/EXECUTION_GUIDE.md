# Step-by-Step Execution Guide - Customized for Your Setup

## ✅ Your Configuration Summary

- **User WITHOUT MFA**: `nuwanthapiumal57@gmail.com` / `Nuwantha@1234`
- **User WITH MFA**: `nuwanthapiumal57+test3@gmail.com` / `Udara@1234`
- **Authenticator App**: Microsoft Authenticator
- **Java Version**: Java 21
- **Device**: `emulator-5554`
- **APK Path**: `C:/Intern_Project_Code/task-01-mobile/build/app/outputs/flutter-apk/app-debug.apk`

---

## 🎯 Complete Execution Steps

### Prerequisites Check

Run these commands to verify your setup:

```powershell
# Check Java version (should be 21)
java -version

# Check Maven
mvn -version

# Check Node.js
node -v

# Check Appium
appium -v

# If Appium not installed:
npm install -g appium
appium driver install uiautomator2
```

---

### Step 1: Get MFA Secret Key

**⚠️ IMPORTANT: You need this before running tests!**

Choose one method from `GET_MFA_SECRET.md`:

#### **Recommended: Re-setup MFA Method**

1. **Open AWS Cognito Console:**

   - Go to AWS Cognito → User Pools
   - Find user: `nuwanthapiumal57+test3@gmail.com`
   - Remove MFA device

2. **Re-setup via Flutter App:**

   - Start emulator: `emulator-5554`
   - Open app on emulator
   - Login with: `nuwanthapiumal57+test3@gmail.com` / `Udara@1234`
   - Navigate to "Setup MFA"
   - **Click "Copy secret key"** button
   - Save this key (something like: `JBSWY3DPEHPK3PXP`)

3. **Complete MFA setup in Microsoft Authenticator**

4. **Update config file:**
   ```powershell
   # Edit: appium-tests\src\test\resources\config.properties
   # Change line:
   test.mfa.secret=JBSWY3DPEHPK3PXP  # Your actual secret key
   ```

---

### Step 2: Build Flutter App

```powershell
cd C:\Intern_Project_Code\task-01-mobile

# Build debug APK
flutter build apk --debug

# Verify APK was created
dir build\app\outputs\flutter-apk\app-debug.apk
```

**Expected output**: APK file should exist at that path

---

### Step 3: Install Maven Dependencies

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests

# Clean and install dependencies
mvn clean install -DskipTests
```

**Expected output**: `BUILD SUCCESS`

---

### Step 4: Start Appium Server

**Open a NEW PowerShell window:**

```powershell
# Start Appium
appium

# Keep this window open!
```

**Expected output**:

```
[Appium] Welcome to Appium v2.x.x
[Appium] Appium REST http interface listener started on 0.0.0.0:4723
```

---

### Step 5: Start Android Emulator

**Option A - Android Studio:**

- Open Android Studio
- Click "Device Manager"
- Start your emulator

**Option B - Command Line:**

```powershell
# List available emulators
emulator -list-avds

# Start the emulator
emulator -avd YOUR_AVD_NAME
```

**Verify emulator is running:**

```powershell
adb devices
```

**Expected output**:

```
List of devices attached
emulator-5554   device
```

---

### Step 6: Run Individual Tests (First Time)

**Test 1: Navigate to MFA Setup**

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests

mvn test -Dtest=MfaTest#testNavigateToMfaSetup
```

**What this does:**

- Opens app on emulator
- Logs in with user without MFA: `nuwanthapiumal57@gmail.com`
- Navigates to Setup MFA screen
- Verifies it's displayed

**Expected**: ✅ Test passes

---

**Test 2: Verify QR Code and Secret Key**

```powershell
mvn test -Dtest=MfaTest#testQrCodeAndSecretKeyDisplayed
```

**What this does:**

- Checks QR code is visible
- Extracts and displays secret key
- Verifies all 3 steps are shown

**Expected**: ✅ Test passes, shows secret key in console

---

**Test 3: Setup MFA with Valid Code**

```powershell
mvn test -Dtest=MfaTest#testMfaSetupWithValidCode
```

**What this does:**

- Gets secret key from UI
- Generates valid TOTP code automatically
- Enters code and completes MFA setup
- Verifies success screen

**Expected**: ✅ MFA is now enabled for `nuwanthapiumal57@gmail.com`

---

**Test 4: Login with MFA**

```powershell
mvn test -Dtest=MfaTest#testLoginWithMfaValidCode
```

**What this does:**

- Logs out
- Logs in with MFA user: `nuwanthapiumal57+test3@gmail.com`
- Automatically generates TOTP code from secret key
- Enters code and completes login

**Expected**: ✅ Test passes, successfully logs in

---

### Step 7: Run Complete Test Suite

Once individual tests pass, run all tests:

```powershell
mvn test
```

**Expected output:**

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.mobile.tests.MfaTest
...
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0

[INFO] BUILD SUCCESS
```

---

## 📊 Test Results

### View HTML Report

```powershell
# Open TestNG report
start test-output\index.html

# Or navigate to:
explorer test-output
```

### View Console Output

Check console for:

- Generated TOTP codes
- Secret keys
- Test execution details
- Any errors

---

## 🐛 Troubleshooting

### Issue: "Cannot find APK"

```powershell
# Rebuild APK
cd C:\Intern_Project_Code\task-01-mobile
flutter clean
flutter build apk --debug

# Verify path
dir build\app\outputs\flutter-apk\app-debug.apk
```

---

### Issue: "No devices found"

```powershell
# Check ADB
adb devices

# If no devices:
# 1. Start emulator
# 2. Or restart ADB
adb kill-server
adb start-server
adb devices
```

---

### Issue: "Connection refused to Appium"

```powershell
# Check if Appium is running
# You should see it running in another PowerShell window

# If not, start it:
appium

# Verify it's listening on port 4723
```

---

### Issue: "Invalid TOTP code"

**Possible causes:**

1. **Secret key not updated** → Check `config.properties`
2. **Wrong secret key** → Re-do MFA setup and copy correct key
3. **Time sync issue** → Ensure emulator time is correct

**Fix:**

```powershell
# Verify secret key in config
notepad appium-tests\src\test\resources\config.properties

# Check the value of:
test.mfa.secret=YOUR_KEY_HERE
```

---

### Issue: "Element not found"

**Possible causes:**

1. App is loading slowly
2. UI locators changed

**Fix:**

- Increase wait times in tests
- Check if app is fully loaded
- Verify emulator performance

---

## 🎯 Quick Commands Cheat Sheet

```powershell
# Build app
cd C:\Intern_Project_Code\task-01-mobile
flutter build apk --debug

# Install test dependencies
cd appium-tests
mvn clean install -DskipTests

# Start Appium (in new window)
appium

# Check devices
adb devices

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=MfaTest#testNavigateToMfaSetup

# View report
start test-output\index.html

# Clean Maven
mvn clean
```

---

## ✅ Final Checklist Before Running

- [ ] Java 21 installed and verified
- [ ] Maven installed and verified
- [ ] Appium installed and running
- [ ] Emulator `emulator-5554` running
- [ ] APK built at correct path
- [ ] `config.properties` updated with:
  - [ ] User credentials
  - [ ] MFA secret key
  - [ ] Device name: `emulator-5554`
- [ ] Maven dependencies installed
- [ ] Microsoft Authenticator has MFA for test3 user

---

## 🚀 Ready to Start?

1. **Get MFA secret key** (see `GET_MFA_SECRET.md`)
2. **Update config.properties** with secret key
3. **Follow Step 1-7 above**
4. **Run tests and enjoy!** 🎉

---

**Need help? Check:**

- `GET_MFA_SECRET.md` - How to get secret key
- `README.md` - Full documentation
- `QUICK_START.md` - Quick reference

**Good luck with your tests! 🚀**
