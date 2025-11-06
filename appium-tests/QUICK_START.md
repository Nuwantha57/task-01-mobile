# Quick Setup Guide for MFA Appium Tests

## Before You Start - Answer These Questions:

### 1. Test Environment

- [ ] Do you have Android Studio installed?
- [ ] Do you have an Android Emulator or physical device ready?
- [ ] Is Java JDK 17+ installed? Check with: `java -version`
- [ ] Is Maven installed? Check with: `mvn -version`

### 2. Appium Setup

- [ ] Is Node.js installed? Check with: `node -v`
- [ ] Is Appium installed? If not, run: `npm install -g appium`
- [ ] Is UiAutomator2 driver installed? If not, run: `appium driver install uiautomator2`

### 3. Test Users

You need **TWO** test user accounts:

**User 1: Without MFA** (for testing MFA setup flow)

- Email: **********\_\_\_**********
- Password: ********\_\_\_\_********

**User 2: With MFA Already Enabled** (for testing login with MFA)

- Email: **********\_\_\_**********
- Password: ********\_\_\_\_********
- MFA Secret Key: ******\_\_******

> **Note**: You'll get the secret key after setting up MFA for the first time.

### 4. App Build

- [ ] Is the Flutter app built? If not, run:
  ```bash
  cd C:\Intern_Project_Code\task-01-mobile
  flutter build apk --debug
  ```

---

## 🚀 Quick Start Steps

### Step 1: Install Dependencies

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests
mvn clean install -DskipTests
```

### Step 2: Configure Tests

Edit `src/test/resources/config.properties`:

```properties
# 1. Update APK path (check this path exists)
app.path=C:/Intern_Project_Code/task-01-mobile/build/app/outputs/flutter-apk/app-debug.apk

# 2. Update device name (run: adb devices to get name)
device.name=emulator-5554

# 3. Update test user credentials
test.email=YOUR_EMAIL_HERE
test.password=YOUR_PASSWORD_HERE

test.email.with.mfa=YOUR_MFA_USER_EMAIL_HERE
test.password.with.mfa=YOUR_MFA_PASSWORD_HERE

# 4. MFA Secret - Leave as is for now, update after first test
test.mfa.secret=YOUR_SECRET_KEY_HERE
```

### Step 3: Start Appium Server

Open a **NEW** PowerShell window:

```powershell
appium
```

Leave this running!

### Step 4: Start Android Emulator

Option A - From Android Studio:

- Open Android Studio → AVD Manager → Click Play ▶️

Option B - From Command Line:

```powershell
emulator -list-avds
emulator -avd Pixel_5_API_33  # Use your AVD name
```

### Step 5: Get MFA Secret Key

**First-time setup:**

1. Run ONLY the secret key test:

```powershell
cd C:\Intern_Project_Code\task-01-mobile\appium-tests
mvn test -Dtest=MfaTest#testQrCodeAndSecretKeyDisplayed
```

2. Check console output for:

```
Secret Key (save this for future tests): JBSWY3DPEHPK3PXP
```

3. Copy this key and update `config.properties`:

```properties
test.mfa.secret=JBSWY3DPEHPK3PXP  # Your actual key
```

### Step 6: Complete MFA Setup for Test User

Run the setup tests:

```powershell
mvn test -Dtest=MfaTest#testMfaSetupWithValidCode
```

This will:

- Navigate to MFA setup
- Generate TOTP code automatically
- Complete MFA setup
- Save MFA for the test account

### Step 7: Setup Second Test User

**Manually setup MFA for second test user:**

1. Open app on emulator
2. Login with `test.email.with.mfa` credentials
3. Go to Setup MFA
4. Click "Copy secret key"
5. Update `config.properties` with this secret
6. Complete MFA setup in the app

### Step 8: Run All Tests

```powershell
mvn test
```

---

## ✅ Verification Checklist

Before running full test suite:

- [ ] Appium server is running (check PowerShell window)
- [ ] Android emulator/device is running (`adb devices` shows device)
- [ ] APK path is correct in config.properties
- [ ] Test user #1 credentials are correct
- [ ] Test user #2 credentials are correct
- [ ] MFA secret key is updated in config.properties
- [ ] MFA is enabled for test user #2

---

## 🎯 Test Execution Order

**Recommended order for first-time run:**

1. **Get Secret Key:**

   ```powershell
   mvn test -Dtest=MfaTest#testQrCodeAndSecretKeyDisplayed
   ```

2. **Setup MFA:**

   ```powershell
   mvn test -Dtest=MfaTest#testMfaSetupWithValidCode
   ```

3. **Test Login with MFA:**

   ```powershell
   mvn test -Dtest=MfaTest#testLoginWithMfaValidCode
   ```

4. **Run All Tests:**
   ```powershell
   mvn test
   ```

---

## 📊 Expected Results

✅ **10 tests total**

- 4 tests for MFA setup flow
- 4 tests for login with MFA
- 2 tests for validation

**Success looks like:**

```
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🐛 Common Issues & Quick Fixes

### ❌ "Cannot find APK"

```powershell
# Build the app
cd C:\Intern_Project_Code\task-01-mobile
flutter build apk --debug
```

### ❌ "Connection refused to Appium"

```powershell
# Start Appium in a new window
appium
```

### ❌ "No devices found"

```powershell
# Check devices
adb devices

# Start emulator
emulator -avd YOUR_AVD_NAME
```

### ❌ "Invalid TOTP code"

- Ensure device/emulator time is correct
- Check secret key is correct
- TOTP codes expire every 30 seconds - try again

### ❌ "Element not found"

- App UI might have changed
- Check if app is properly loaded
- Wait a bit longer (increase wait times)

---

## 📝 Quick Commands Reference

```powershell
# Install dependencies
mvn clean install -DskipTests

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=MfaTest#testNavigateToMfaSetup

# Check devices
adb devices

# Start Appium
appium

# Build app
flutter build apk --debug

# View test report
start test-output\index.html
```

---

## 📞 Need Help?

1. **Read full README**: `appium-tests/README.md`
2. **Check test logs**: `appium-tests/test-output/`
3. **Review MFA guide**: `MFA_IMPLEMENTATION_GUIDE.md`

---

**Ready to start? Begin with Step 1! 🚀**
