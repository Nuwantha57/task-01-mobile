# MFA Testing Checklist

## Prerequisites

- ✅ AWS Cognito User Pool with MFA enabled
- ✅ Authenticator app installed (Google Authenticator, Microsoft Authenticator, etc.)
- ✅ Test user account created

## Test Scenarios

### ✅ Scenario 1: Enable MFA (First Time)

**Steps:**

1. Sign in with email and password
2. Navigate to Home screen
3. Tap "Setup MFA" button
4. Verify QR code is displayed
5. Scan QR code with authenticator app
6. Enter 6-digit code from authenticator
7. Tap "Verify and Enable MFA"
8. Verify success message appears

**Expected Result:**

- QR code renders correctly
- Secret key is displayed with copy option
- Verification succeeds with correct code
- Success screen shows "MFA Successfully Enabled"

---

### ✅ Scenario 2: Sign In with MFA Enabled

**Steps:**

1. Sign out completely
2. Sign in with email and password
3. Wait for MFA prompt
4. Open authenticator app
5. Enter current 6-digit code
6. Tap "Verify"

**Expected Result:**

- After password, MFA confirmation screen appears
- Correct code grants access to home screen
- Incorrect code shows error message
- Expired code shows error message

---

### ✅ Scenario 3: Invalid MFA Code

**Steps:**

1. Sign in with credentials
2. Enter wrong MFA code (e.g., "000000")
3. Tap "Verify"

**Expected Result:**

- Error message: "MFA confirmation failed"
- User remains on MFA screen
- Can retry with correct code

---

### ✅ Scenario 4: Manual Secret Key Entry

**Steps:**

1. During MFA setup, scroll down
2. Locate "Can't scan? Enter manually:" section
3. Tap copy icon to copy secret key
4. Add manually to authenticator app
5. Enter verification code

**Expected Result:**

- Secret key is copyable
- Manual entry works same as QR scan
- Verification succeeds

---

### ✅ Scenario 5: Expired Code

**Steps:**

1. Sign in with credentials
2. Wait for code to change in authenticator (30+ seconds)
3. Enter the OLD code
4. Tap "Verify"

**Expected Result:**

- Shows error message
- User can enter new code and retry

---

## Edge Cases to Test

### ❌ Without Internet Connection

- MFA setup should fail gracefully
- Show appropriate error message

### ❌ User Cancels MFA Setup

- Can navigate back without issues
- Can restart setup later

### ❌ User Already Has MFA Enabled

- Setup screen should handle gracefully
- Or prevent navigation to setup screen

---

## Test Data

### Test Users

Create test users with different states:

1. **User without MFA**: Fresh account, never set up MFA
2. **User with MFA**: Already has TOTP configured
3. **User with wrong time**: Device time not synced (to test time-based issues)

---

## Common Issues & Solutions

| Issue               | Cause             | Solution                  |
| ------------------- | ----------------- | ------------------------- |
| "Invalid MFA code"  | Time not synced   | Sync device time          |
| QR code not showing | Network issue     | Check internet connection |
| Code always invalid | Wrong secret key  | Re-setup MFA              |
| Can't scan QR       | Camera permission | Use manual entry          |

---

## AWS Cognito Verification

After enabling MFA for a user:

1. Go to AWS Console → Cognito → Users
2. Find the test user
3. Check user attributes for MFA status
4. Verify "MFAOptions" or "PreferredMFA" is set

---

## Manual Testing Commands

### Check Amplify Configuration

```dart
// In your Flutter app
final session = await Amplify.Auth.fetchAuthSession();
print('Is signed in: ${session.isSignedIn}');
```

### Verify TOTP Setup

```dart
// During setup
try {
  final uri = await Amplify.Auth.setUpTotp();
  print('TOTP URI: $uri');
} catch (e) {
  print('Error: $e');
}
```

---

## Success Criteria

MFA implementation is complete when:

- ✅ Users can enable MFA from home screen
- ✅ QR code displays correctly
- ✅ Verification succeeds with valid codes
- ✅ Login requires MFA after enabling
- ✅ Error handling is graceful
- ✅ UI is intuitive and clear
- ✅ Works on both Android and iOS

---

**Last Updated**: November 6, 2025
