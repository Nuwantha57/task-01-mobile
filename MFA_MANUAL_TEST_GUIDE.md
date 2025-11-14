# MFA Manual Testing Guide

**Date:** November 6, 2025  
**Feature:** Multi-Factor Authentication (MFA) using TOTP  
**Testing Flow:** Login → Setup MFA → Verify MFA

---

## Prerequisites

### Required Tools

- ✅ Flutter app installed on device/emulator
- ✅ Authenticator app installed (Google Authenticator, Microsoft Authenticator, or Authy)
- ✅ Valid test credentials (email and password)

### Test Environment

- **Platform:** Android/iOS
- **Authentication:** AWS Cognito
- **MFA Type:** TOTP (Time-based One-Time Password)

---

## Complete Testing Flow

### Phase 1: Initial Login (Without MFA)

#### Test Case 1.1: Login with Valid Credentials

**Objective:** Successfully login to access the home screen

**Steps:**

1. Launch the app
2. On the login screen, enter your email
3. Enter your password
4. Tap "Sign In" button

**Expected Results:**

- ✅ Loading indicator appears
- ✅ Successfully navigate to Home Screen
- ✅ User information displayed (name, email)
- ✅ "Setup MFA" option visible in action cards

**Test Data:**

```
Email: [Your test email]
Password: [Your test password]
```

**Screenshot Points:**

- Login screen with filled credentials
- Home screen after successful login

---

### Phase 2: MFA Setup

#### Test Case 2.1: Navigate to MFA Setup

**Objective:** Access the MFA setup screen

**Steps:**

1. From Home Screen, locate "Setup MFA" card
2. Verify card shows:
   - Icon: Security/Lock icon
   - Title: "Setup MFA"
   - Subtitle: "Enable two-factor authentication"
3. Tap the "Setup MFA" card

**Expected Results:**

- ✅ Navigate to MFA Setup screen
- ✅ Screen title shows "Setup MFA"
- ✅ Loading indicator appears while generating QR code

**Screenshot Points:**

- Home screen with Setup MFA card highlighted
- MFA Setup screen loading

---

#### Test Case 2.2: QR Code Generation and Display

**Objective:** Verify QR code is properly generated and displayed

**Steps:**

1. Wait for QR code to load
2. Observe the screen layout

**Expected Results:**

- ✅ Security icon displayed at top
- ✅ Title: "Enable Two-Factor Authentication"
- ✅ Subtitle: "Scan the QR code with your authenticator app"
- ✅ Step 1: "Install an Authenticator App" with description
- ✅ Step 2: "Scan QR Code" with description
- ✅ QR code displays in white container with shadow
- ✅ QR code is clearly visible and scannable
- ✅ Manual entry section visible with secret key
- ✅ Step 3: "Enter Verification Code" visible
- ✅ 6-digit input field displayed
- ✅ "Verify and Enable MFA" button visible

**Screenshot Points:**

- Full MFA setup screen with QR code
- Close-up of QR code
- Manual entry section

---

#### Test Case 2.3: Manual Secret Key Copy

**Objective:** Test manual entry option

**Steps:**

1. Scroll to "Can't scan? Enter manually:" section
2. Observe the secret key displayed
3. Tap the copy icon next to the secret key
4. Check clipboard notification

**Expected Results:**

- ✅ Secret key displayed in monospace font
- ✅ Copy icon visible and clickable
- ✅ Snackbar appears: "Secret key copied to clipboard"
- ✅ Secret key is in clipboard (can paste elsewhere to verify)

**Test Variations:**

- Copy and paste secret key to notes app to verify it's copied correctly

---

#### Test Case 2.4: Scan QR Code

**Objective:** Add account to authenticator app

**Steps:**

1. Open your authenticator app (Google Authenticator/Microsoft Authenticator/Authy)
2. Tap "Add Account" or "+" button
3. Select "Scan QR Code" or "Scan barcode"
4. Point camera at QR code on screen
5. Wait for scan to complete

**Expected Results:**

- ✅ QR code scans successfully
- ✅ Account added to authenticator with label "StaffAuth" or similar
- ✅ 6-digit code appears and updates every 30 seconds
- ✅ Account shows email/username

**Screenshot Points:**

- Authenticator app showing the new account
- 6-digit code displayed in authenticator

---

#### Test Case 2.5: Enter Valid Verification Code

**Objective:** Verify MFA setup with correct code

**Steps:**

1. Look at the 6-digit code in authenticator app
2. In the Flutter app, tap the verification code input field
3. Enter the 6-digit code (ensure code hasn't expired)
4. Tap "Verify and Enable MFA" button

**Expected Results:**

- ✅ Keyboard appears for number input
- ✅ Input accepts exactly 6 digits
- ✅ Input displays with letter spacing
- ✅ Button shows loading indicator during verification
- ✅ Success screen appears with:
  - Green check circle icon
  - Title: "MFA Successfully Enabled!"
  - Description explaining MFA is now active
  - "Done" button
- ✅ Green snackbar: "MFA has been successfully enabled for your account!"

**Screenshot Points:**

- Verification code input with 6 digits
- Loading state on button
- Success screen

---

#### Test Case 2.6: Invalid Verification Code

**Objective:** Test error handling for wrong code

**Steps:**

1. Enter an incorrect 6-digit code (e.g., "000000" or "123456")
2. Tap "Verify and Enable MFA" button

**Expected Results:**

- ✅ Red snackbar appears with error message
- ✅ Error message: "MFA confirmation failed" or similar
- ✅ User remains on setup screen
- ✅ Can retry with correct code

**Screenshot Points:**

- Error message displayed

---

#### Test Case 2.7: Expired Verification Code

**Objective:** Test timing validation

**Steps:**

1. Note the current code in authenticator
2. Wait for the code to change (30 seconds)
3. Enter the OLD code that just expired
4. Tap "Verify and Enable MFA"

**Expected Results:**

- ✅ Error message appears
- ✅ Can enter new code and succeed

---

#### Test Case 2.8: Complete MFA Setup

**Objective:** Finish setup and return to home

**Steps:**

1. Successfully verify code
2. On success screen, tap "Done" button

**Expected Results:**

- ✅ Navigate back to Home Screen or previous screen
- ✅ MFA is now enabled for the account

**Screenshot Points:**

- Return to home screen

---

### Phase 3: Login with MFA Enabled

#### Test Case 3.1: Logout

**Objective:** Sign out to test MFA login flow

**Steps:**

1. From Home Screen, find logout option
2. Tap logout
3. Confirm logout if prompted

**Expected Results:**

- ✅ Successfully logged out
- ✅ Navigate back to Login Screen
- ✅ Credentials cleared

---

#### Test Case 3.2: Login with MFA Challenge

**Objective:** Test complete login flow with MFA

**Steps:**

1. On Login Screen, enter email
2. Enter password
3. Tap "Sign In"
4. Wait for navigation

**Expected Results:**

- ✅ After password validation, navigates to MFA Confirmation Screen
- ✅ Screen shows:
  - Lock icon
  - Title: "Enter MFA Code"
  - Description about entering code
  - 6-digit input field
  - "Verify" button
- ✅ NOT navigated directly to Home Screen

**Screenshot Points:**

- MFA Confirmation screen

---

#### Test Case 3.3: Enter Valid MFA Code at Login

**Objective:** Complete login with MFA

**Steps:**

1. Open authenticator app
2. Find the account
3. Note the current 6-digit code
4. In Flutter app, enter the code
5. Tap "Verify"

**Expected Results:**

- ✅ Code accepted
- ✅ Navigate to Home Screen
- ✅ User successfully logged in

**Screenshot Points:**

- Successful home screen access

---

#### Test Case 3.4: Enter Invalid MFA Code at Login

**Objective:** Test error handling during login

**Steps:**

1. Enter wrong code (e.g., "999999")
2. Tap "Verify"

**Expected Results:**

- ✅ Error message appears
- ✅ Remain on MFA Confirmation screen
- ✅ Can retry with correct code

---

### Phase 4: UI/UX Testing

#### Test Case 4.1: Keyboard Behavior

**Objective:** Test input field interactions

**Steps:**

1. Navigate to MFA Setup screen
2. Tap on verification code input
3. Observe keyboard
4. Enter digits
5. Tap outside input field

**Expected Results:**

- ✅ Numeric keyboard appears
- ✅ Only digits can be entered
- ✅ Maximum 6 digits enforced
- ✅ Keyboard dismisses when tapping outside
- ✅ No infinite keyboard show/cancel loop

**Note:** This was a previous issue that should be fixed with FocusNode management

---

#### Test Case 4.2: Responsive Design

**Objective:** Test layout on different screen sizes

**Steps:**

1. Test on phone (if available)
2. Rotate device to landscape
3. Test on tablet (if available)

**Expected Results:**

- ✅ QR code scales appropriately
- ✅ All content visible without overflow
- ✅ Buttons accessible
- ✅ Scrolling works smoothly

---

#### Test Case 4.3: Loading States

**Objective:** Verify loading indicators

**Steps:**

1. Observe loading during QR generation
2. Observe loading during verification
3. Note timing and responsiveness

**Expected Results:**

- ✅ Loading indicator shown during async operations
- ✅ UI is disabled during loading
- ✅ No crashes during loading

---

### Phase 5: Edge Cases

#### Test Case 5.1: Rapid Button Tapping

**Objective:** Test duplicate submission prevention

**Steps:**

1. Enter valid code
2. Rapidly tap "Verify and Enable MFA" multiple times

**Expected Results:**

- ✅ Button disabled after first tap
- ✅ Only one verification request sent
- ✅ No duplicate success messages

---

#### Test Case 5.2: Network Interruption

**Objective:** Test offline behavior

**Steps:**

1. During setup, turn off WiFi/mobile data
2. Try to verify code

**Expected Results:**

- ✅ Appropriate error message
- ✅ No app crash
- ✅ Can retry when connection restored

---

#### Test Case 5.3: App Backgrounding

**Objective:** Test state preservation

**Steps:**

1. During MFA setup, press home button
2. Return to app after few seconds

**Expected Results:**

- ✅ QR code still visible
- ✅ No need to regenerate
- ✅ Can continue setup

---

## Test Results Template

### Test Execution Summary

**Tester:** ********\_\_\_********  
**Date:** ********\_\_\_********  
**Device:** ********\_\_\_********  
**OS Version:** ********\_\_\_********  
**App Version:** ********\_\_\_********

| Test Case               | Status          | Notes | Screenshots |
| ----------------------- | --------------- | ----- | ----------- |
| 1.1 - Initial Login     | ⬜ Pass ⬜ Fail |       |             |
| 2.1 - Navigate to Setup | ⬜ Pass ⬜ Fail |       |             |
| 2.2 - QR Code Display   | ⬜ Pass ⬜ Fail |       |             |
| 2.3 - Copy Secret Key   | ⬜ Pass ⬜ Fail |       |             |
| 2.4 - Scan QR Code      | ⬜ Pass ⬜ Fail |       |             |
| 2.5 - Valid Code        | ⬜ Pass ⬜ Fail |       |             |
| 2.6 - Invalid Code      | ⬜ Pass ⬜ Fail |       |             |
| 2.7 - Expired Code      | ⬜ Pass ⬜ Fail |       |             |
| 2.8 - Complete Setup    | ⬜ Pass ⬜ Fail |       |             |
| 3.1 - Logout            | ⬜ Pass ⬜ Fail |       |             |
| 3.2 - MFA Challenge     | ⬜ Pass ⬜ Fail |       |             |
| 3.3 - Valid MFA Login   | ⬜ Pass ⬜ Fail |       |             |
| 3.4 - Invalid MFA Login | ⬜ Pass ⬜ Fail |       |             |
| 4.1 - Keyboard Behavior | ⬜ Pass ⬜ Fail |       |             |
| 4.2 - Responsive Design | ⬜ Pass ⬜ Fail |       |             |
| 4.3 - Loading States    | ⬜ Pass ⬜ Fail |       |             |
| 5.1 - Rapid Tapping     | ⬜ Pass ⬜ Fail |       |             |
| 5.2 - Network Error     | ⬜ Pass ⬜ Fail |       |             |
| 5.3 - Backgrounding     | ⬜ Pass ⬜ Fail |       |             |

### Bugs Found

| Bug ID | Severity | Description | Steps to Reproduce | Status |
| ------ | -------- | ----------- | ------------------ | ------ |
|        |          |             |                    |        |

### Overall Assessment

**Total Tests:** **_  
**Passed:** _**  
**Failed:** **_  
**Pass Rate:** _**%

**Overall Status:** ⬜ Approved ⬜ Needs Fixes

**Comments:**

---

---

---

---

## Quick Test Commands

### Check if user has MFA enabled (AWS CLI)

```bash
aws cognito-idp get-user --access-token <access_token>
```

### Test MFA Code Generation (Manual)

Use any TOTP generator with the secret key to verify codes match.

---

## Troubleshooting Guide

### Issue: QR Code Not Displaying

- Check internet connection
- Verify AWS Cognito configuration
- Check console for errors
- Verify user has permission to setup TOTP

### Issue: Code Verification Fails

- Ensure code is current (not expired)
- Verify time sync on device
- Check authenticator app time settings
- Try manual entry instead of QR scan

### Issue: Keyboard Issues

- Verify FocusNode is properly implemented
- Check for infinite loops in logs
- Test on different Android versions

### Issue: Navigation Problems

- Verify Navigator push/pop logic
- Check for context issues
- Review route management

---

## Success Criteria

✅ **Setup Flow**

- User can successfully scan QR code
- User can manually enter secret key
- Verification works with valid codes
- Appropriate errors shown for invalid codes

✅ **Login Flow**

- MFA prompt appears after password
- Valid codes grant access
- Invalid codes show errors and allow retry

✅ **UI/UX**

- No keyboard loops or focus issues
- Loading states clear and responsive
- Error messages helpful and clear
- Navigation smooth and logical

✅ **Security**

- MFA properly enforced after setup
- Codes validated server-side
- No bypass mechanisms

---

## Notes for Tester

1. **Test with fresh account:** First time testing should be with account that has never enabled MFA
2. **Test authenticator variations:** Try different authenticator apps if possible
3. **Document timing:** Note how long operations take
4. **Screenshot everything:** Especially errors and success states
5. **Test on multiple devices:** If available, test on different Android versions
6. **Network conditions:** Test on both WiFi and mobile data
7. **Time zones:** Verify codes work across time zone changes

---

## Contact

**Questions or Issues?**

- Check existing documentation in `/MFA_IMPLEMENTATION_GUIDE.md`
- Review `/MFA_TESTING_CHECKLIST.md`
- Check logs for detailed error messages
