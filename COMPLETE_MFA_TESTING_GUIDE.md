# 🧪 MFA Testing - Complete Guide

## ⚠️ IMPORTANT: Pre-Testing Setup

### Step 1: Start the App Manually

Since automated terminal commands may have issues, please start the app manually:

```bash
# In your terminal (PowerShell or CMD), navigate to project root
cd C:\Intern_Project_Code\task-01-mobile

# Run the app
flutter run
```

**Wait for the app to fully launch** - you should see:

- ✅ "Flutter run key commands" message
- ✅ App running on your emulator/device
- ✅ Login screen visible

---

## 🎯 Complete MFA Testing Flow

### TEST 1: Initial Login (No MFA Yet)

**Prerequisites:**

- App is running on emulator
- You have test credentials ready

**Steps:**

1. Look at your emulator - you should see the **Login Screen**
2. Enter your email address
3. Enter your password
4. Tap **"Sign In"** button
5. **Expected:** You should navigate to the **Home Screen**

**✅ Success Criteria:**

- Login works without MFA prompt
- Home screen shows your user information
- "Setup MFA" card is visible

**📸 Screenshot:** Home screen with "Setup MFA" button

---

### TEST 2: MFA Setup Process

#### Step 2.1: Navigate to MFA Setup

**Steps:**

1. On the Home Screen, scroll down to find **"Setup MFA"** card
2. The card should have:
   - 🔒 Security icon
   - Title: "Setup MFA"
   - Subtitle: "Enable two-factor authentication"
3. **Tap the "Setup MFA" card**

**✅ Success Criteria:**

- Navigate to MFA Setup Screen
- Loading indicator appears

**📸 Screenshot:** MFA Setup screen loading

---

#### Step 2.2: QR Code Display

**Steps:**

1. Wait for the QR code to generate (usually 1-3 seconds)
2. Observe the screen

**✅ Success Criteria:**

- QR code displays clearly in a white container
- Step-by-step instructions visible:
  - Step 1: Install an Authenticator App
  - Step 2: Scan QR Code
  - Step 3: Enter Verification Code
- Manual entry section visible with secret key
- 6-digit input field visible
- "Verify and Enable MFA" button visible

**📸 Screenshots:**

- Full MFA setup screen
- Close-up of QR code

**⚠️ If QR Code Doesn't Appear:**

- Check your internet connection
- Check console logs for errors
- Try hot restart: Press `R` in the terminal

---

#### Step 2.3: Copy Secret Key (Optional Test)

**Steps:**

1. Scroll to the "Can't scan? Enter manually:" section
2. You should see the secret key in monospace font
3. Tap the **copy icon** (📋) next to the secret key
4. Look for the snackbar message

**✅ Success Criteria:**

- Snackbar appears: "Secret key copied to clipboard"
- You can paste the key into a notes app to verify

**Note:** Write down the secret key here for reference:

```
Secret Key: _________________________________
```

---

#### Step 2.4: Scan QR Code with Authenticator App

**Steps:**

1. **Open your authenticator app** (Google Authenticator, Microsoft Authenticator, or Authy)
2. Tap **"+"** or **"Add Account"**
3. Select **"Scan QR Code"** or **"Scan Barcode"**
4. **Point your phone's camera** at the QR code on the emulator screen
5. Wait for the scan to complete

**✅ Success Criteria:**

- Account added successfully to authenticator
- Account name shows something like "StaffAuth" or your app name
- 6-digit code appears
- Code updates every 30 seconds

**📸 Screenshot:** Authenticator app showing the new account with code

**⚠️ If Scanning Doesn't Work:**

- Use the manual entry method:
  1. In authenticator, select "Enter a setup key" or "Manual entry"
  2. Enter account name: "StaffAuth" (or any name you like)
  3. Paste the secret key you copied earlier
  4. Keep time-based selected
  5. Save

---

#### Step 2.5: Enter Verification Code (Valid Code)

**Steps:**

1. Look at your authenticator app
2. Note the current 6-digit code (e.g., `123456`)
3. In the Flutter app, **tap the verification code input field**
4. **Keyboard should appear** (numeric keyboard on Android)
5. **Enter the 6 digits** from your authenticator app
6. **Tap "Verify and Enable MFA"** button

**✅ Success Criteria:**

- Input field accepts exactly 6 digits
- Code displays with letter spacing
- Button shows loading indicator (spinner)
- **Success screen appears** with:
  - ✅ Green checkmark icon
  - Title: "MFA Successfully Enabled!"
  - Description text
  - "Done" button
- Green snackbar message appears

**📸 Screenshots:**

- Input field with code entered
- Loading button
- Success screen

**⏱️ IMPORTANT TIMING:**

- TOTP codes expire every 30 seconds
- If you see the code change in your authenticator, use the NEW code
- If verification fails, the code may have expired - try the new code

---

#### Step 2.6: Test Invalid Code (Error Handling)

**Note:** You can skip this test if you want, or do it later by setting up MFA for another account.

**Steps:**

1. (Start MFA setup again with a different account, or skip to next test)
2. Instead of entering the correct code, enter `000000`
3. Tap "Verify and Enable MFA"

**✅ Success Criteria:**

- Red snackbar appears with error message
- Error message says something like "MFA confirmation failed" or "Code not valid"
- You remain on the setup screen (not navigated away)
- You can try again with the correct code

**📸 Screenshot:** Error message

---

#### Step 2.7: Complete Setup

**Steps:**

1. On the success screen, tap the **"Done"** button

**✅ Success Criteria:**

- Navigate back to Home Screen (or previous screen)
- MFA is now enabled for your account

---

### TEST 3: Login with MFA Enabled

#### Step 3.1: Logout

**Steps:**

1. On the Home Screen, look for the **logout button** (usually top-right or in a menu)
2. Tap **logout**
3. If there's a confirmation dialog, tap **"Yes"** or **"Confirm"**

**✅ Success Criteria:**

- Successfully logged out
- Navigate back to Login Screen
- Email/password fields are empty

**📸 Screenshot:** Login screen after logout

---

#### Step 3.2: Login and MFA Challenge

**Steps:**

1. On the Login Screen, **enter your email**
2. **Enter your password**
3. **Tap "Sign In"**
4. **IMPORTANT:** Watch what happens next

**✅ Success Criteria:**

- After password validation, you should **NOT** go directly to Home Screen
- Instead, you should navigate to **MFA Confirmation Screen**
- MFA screen should show:
  - 🔒 Security icon
  - Title: "Enter MFA Code"
  - Description: "Enter the 6-digit code from your authenticator app"
  - 6-digit input field
  - "Verify" button

**📸 Screenshot:** MFA Confirmation screen

**❌ FAILURE:** If you go directly to Home Screen, MFA was not properly enabled

---

#### Step 3.3: Enter Valid MFA Code (Login)

**Steps:**

1. **Open your authenticator app**
2. Find the account you added earlier
3. **Note the current 6-digit code**
4. In the Flutter app (MFA Confirmation screen), **enter the code**
5. **Tap "Verify"**

**✅ Success Criteria:**

- Button shows loading indicator
- Successfully navigate to **Home Screen**
- You are now logged in

**📸 Screenshot:** Home screen after MFA login

**Time this:** How long did the complete login take? **\_\_** seconds

---

#### Step 3.4: Test Invalid MFA Code (Login)

**Steps:**

1. **Logout again**
2. **Login with email and password**
3. At the MFA prompt, **enter a wrong code** (e.g., `999999`)
4. **Tap "Verify"**

**✅ Success Criteria:**

- Red snackbar appears with error message
- You remain on MFA Confirmation screen
- You can try again

5. **Enter the correct code** from your authenticator
6. **Tap "Verify"**
7. Successfully login

**📸 Screenshot:** Error message for invalid MFA code

---

### TEST 4: UI/UX Verification

#### Test 4.1: Keyboard Behavior (CRITICAL)

This tests the fix we applied for the keyboard loop issue.

**Steps:**

1. **Navigate to MFA Setup screen** (you can logout and do setup again, or test during first setup)
2. **Tap the verification code input field**
3. **Watch the terminal/console output carefully**
4. **Look for these patterns:**

**✅ GOOD (Fixed):**

- Keyboard appears once
- No repeated `ImeTracker: onRequestShow` messages
- No repeated `ImeTracker: onCancelled` messages
- No "Lost connection to device" error
- Can type smoothly
- Can tap outside to dismiss keyboard
- Can tap input again to bring keyboard back

**❌ BAD (Bug still exists):**

- Multiple `ImeTracker: onRequestShow` followed by `onCancelled` in rapid succession
- App crashes or loses connection
- Keyboard keeps appearing and disappearing

**📸 Screenshot:** Console logs (if testing)

---

#### Test 4.2: Input Validation

**Steps:**

1. On MFA setup or confirmation screen, tap input field
2. Try to type letters (a, b, c)
3. Try to type special characters (!, @, #)
4. Try to type more than 6 digits

**✅ Success Criteria:**

- Only numeric digits (0-9) are accepted
- Cannot enter more than 6 characters
- No counter text shown below input (counterText: '')
- Input displays with proper letter spacing

---

#### Test 4.3: Loading States

**Steps:**

1. During QR code generation, observe the screen
2. During verification, observe the button
3. Try to tap verify button multiple times rapidly

**✅ Success Criteria:**

- Loading indicator shows during QR generation
- Button shows spinner during verification
- Button is disabled during loading (can't tap multiple times)
- No duplicate submissions

---

### TEST 5: Edge Cases (Optional but Recommended)

#### Test 5.1: Expired Code

**Steps:**

1. At MFA confirmation screen (during login or setup)
2. Look at your authenticator and note the current code
3. **Wait for the code to change** (30 seconds max)
4. **Enter the OLD code** (the one that just disappeared)
5. Tap "Verify"

**✅ Success Criteria:**

- Error message appears (code expired/invalid)
- Can enter new code and succeed

---

#### Test 5.2: App Backgrounding

**Steps:**

1. During MFA setup (with QR code visible)
2. Press **Home button** on your device/emulator
3. Wait 5-10 seconds
4. Return to the app

**✅ Success Criteria:**

- QR code still visible
- Can continue setup
- No crash or data loss

---

#### Test 5.3: Network Interruption (Optional)

**Steps:**

1. Start MFA setup
2. When QR code appears, **turn off WiFi/data**
3. Try to verify with a code

**✅ Success Criteria:**

- Appropriate error message about network
- No crash
- When network restored, can complete setup

---

## 📊 Test Results Summary

### Fill this out after testing:

**Date:** ******\_\_\_******  
**Tester:** ******\_\_\_******  
**Device:** ******\_\_\_******

| Test                      | Status          | Notes        |
| ------------------------- | --------------- | ------------ |
| 1. Initial Login          | ⬜ Pass ⬜ Fail |              |
| 2.1. Navigate to Setup    | ⬜ Pass ⬜ Fail |              |
| 2.2. QR Code Display      | ⬜ Pass ⬜ Fail |              |
| 2.3. Copy Secret Key      | ⬜ Pass ⬜ Fail |              |
| 2.4. Scan QR Code         | ⬜ Pass ⬜ Fail |              |
| 2.5. Valid Code (Setup)   | ⬜ Pass ⬜ Fail |              |
| 2.6. Invalid Code (Setup) | ⬜ Pass ⬜ Fail |              |
| 2.7. Complete Setup       | ⬜ Pass ⬜ Fail |              |
| 3.1. Logout               | ⬜ Pass ⬜ Fail |              |
| 3.2. MFA Challenge        | ⬜ Pass ⬜ Fail |              |
| 3.3. Valid Code (Login)   | ⬜ Pass ⬜ Fail |              |
| 3.4. Invalid Code (Login) | ⬜ Pass ⬜ Fail |              |
| 4.1. Keyboard Behavior    | ⬜ Pass ⬜ Fail | **CRITICAL** |
| 4.2. Input Validation     | ⬜ Pass ⬜ Fail |              |
| 4.3. Loading States       | ⬜ Pass ⬜ Fail |              |

**Total:** \_\_\_/15 tests passed

### Critical Issues Found:

1. ***
2. ***
3. ***

### Overall Assessment:

⬜ **APPROVED** - All critical tests passed, MFA feature works correctly  
⬜ **NEEDS FIXES** - Critical issues found  
⬜ **BLOCKED** - Cannot complete testing

---

## 🐛 Troubleshooting

### Issue: App won't start

**Solution:**

```bash
flutter clean
flutter pub get
flutter run
```

### Issue: "Lost connection to device"

**Solution:** This might be the keyboard bug. Check console for ImeTracker messages. If happening, the keyboard fix didn't work properly.

### Issue: QR code won't scan

**Solution:** Use manual secret key entry instead

### Issue: Code not working

**Solution:**

- Ensure device time is correct (Settings > Date & Time > Automatic)
- Wait for a fresh code (codes change every 30 seconds)
- Verify you're looking at the correct account in authenticator

### Issue: Can't find "Setup MFA" button

**Solution:**

- Ensure you're logged in
- Scroll down on Home Screen
- Check if MFA is already enabled (try logging out and back in)

---

## ✅ Test Complete!

Once you've completed all tests and filled out the results:

1. Take all screenshots
2. Document any bugs found
3. Fill out the results summary
4. Review the critical keyboard behavior test result
5. Make your overall assessment

**Good luck with testing!** 🚀
