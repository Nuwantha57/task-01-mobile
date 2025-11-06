# Getting MFA Secret Key for Testing

Since your test user already has MFA enabled in Microsoft Authenticator, you need to get the secret key. Here are your options:

## ⚠️ Important Note

**Microsoft Authenticator does NOT allow you to export the secret key after setup.**

You have 3 options to get the secret key for automated testing:

---

## Option 1: Re-setup MFA to Get Secret Key (Recommended)

### Steps:

1. **Disable MFA in AWS Cognito Console:**

   - Go to AWS Cognito → User Pools → Users
   - Find user: `nuwanthapiumal57+test3@gmail.com`
   - Delete the MFA device/settings for this user
   - OR set MFA to "Optional" and remove from user attributes

2. **Re-setup MFA via App:**

   - Open the Flutter app on emulator
   - Login with: `nuwanthapiumal57+test3@gmail.com` / `Udara@1234`
   - Go to "Setup MFA"
   - **Before scanning QR code**, click "Copy secret key"
   - Save this secret key → Update `config.properties`
   - Then complete the setup in Microsoft Authenticator

3. **Update config.properties:**
   ```properties
   test.mfa.secret=JBSWY3DPEHPK3PXP  # Your actual secret key
   ```

---

## Option 2: Use Appium Test to Extract Secret Key

### Steps:

1. **First, disable MFA for test user** (in AWS Cognito)

2. **Run the test to extract secret:**

   ```powershell
   cd C:\Intern_Project_Code\task-01-mobile\appium-tests

   # Install dependencies first
   mvn clean install -DskipTests

   # Run secret extraction test
   mvn test -Dtest=MfaTest#testQrCodeAndSecretKeyDisplayed
   ```

3. **Check console output:**

   ```
   Secret Key (save this for future tests): JBSWY3DPEHPK3PXP
   ```

4. **Copy this key and update config.properties**

5. **Complete MFA setup in Microsoft Authenticator** using the app

---

## Option 3: Create New Test User with MFA

If you can't re-setup for existing user:

### Steps:

1. **Create a new test user in AWS Cognito:**

   - Email: `nuwanthapiumal57+test4@gmail.com`
   - Password: `Udara@1234`
   - Confirm email

2. **Setup MFA via app and save secret:**

   - Login to app
   - Go to Setup MFA
   - Click "Copy secret key" → Save it
   - Complete setup in Microsoft Authenticator

3. **Update config.properties:**
   ```properties
   test.email.with.mfa=nuwanthapiumal57+test4@gmail.com
   test.password.with.mfa=Udara@1234
   test.mfa.secret=YOUR_COPIED_SECRET_KEY
   ```

---

## ✅ Recommended Approach for You

Since you're just starting testing, I recommend **Option 1**:

### Quick Steps:

1. **Disable MFA in AWS Cognito:**

   ```
   - AWS Console → Cognito → User Pools
   - Click your pool → Users
   - Find: nuwanthapiumal57+test3@gmail.com
   - Click user → MFA devices → Delete
   ```

2. **Re-setup via Flutter App:**

   ```
   - Open app → Login with test3 account
   - Setup MFA → Copy secret key → Save it
   - Scan QR in Microsoft Authenticator
   ```

3. **Update config and run tests!**

---

## 📝 What to Do After Getting Secret Key

Once you have the secret key:

1. **Update config.properties:**

   ```properties
   test.mfa.secret=JBSWY3DPEHPK3PXP  # Your actual key
   ```

2. **Verify it works:**

   ```powershell
   # This will generate a code and verify it matches
   mvn test -Dtest=MfaTest#testLoginWithMfaValidCode
   ```

3. **Run all tests:**
   ```powershell
   mvn test
   ```

---

## 🔍 How the Automated Tests Work

Once you have the secret key in config.properties:

1. **Tests will automatically generate TOTP codes** using `TotpGenerator.java`
2. **Codes are generated in real-time** (valid for 30 seconds)
3. **No need for Microsoft Authenticator during testing**
4. **Tests run completely automated**

Example:

```java
// Test automatically does this:
String totpCode = TotpGenerator.generateTotpCode(secretKey);
// Generates: "123456" (changes every 30 seconds)
```

---

## ❓ Which Option Should You Choose?

| Option                     | Pros                     | Cons                      | Best For          |
| -------------------------- | ------------------------ | ------------------------- | ----------------- |
| **Option 1: Re-setup**     | Easy, keeps same account | Need to reset MFA         | You (Recommended) |
| **Option 2: Test Extract** | Automated extraction     | Need to disable MFA first | Technical users   |
| **Option 3: New User**     | Keeps existing setup     | Need new user             | Production users  |

---

## 🚀 Next Steps After Getting Secret Key

1. ✅ Update `config.properties` with secret key
2. ✅ Build APK: `flutter build apk --debug`
3. ✅ Start Appium: `appium`
4. ✅ Start Emulator: Device `emulator-5554`
5. ✅ Run tests: `mvn test`

---

**Which option will you use? Let me know and I can guide you through it! 🎯**
