# MFA Implementation Guide

## Overview

This guide explains the MFA (Multi-Factor Authentication) implementation in your Flutter mobile app using AWS Cognito.

## ✅ What Has Been Implemented

### 1. **MFA Setup Screen** (`lib/screens/mfa_setup_screen.dart`)

A comprehensive screen that allows users to enable MFA with:

- QR code generation for authenticator apps
- Manual secret key entry option (with copy to clipboard)
- Step-by-step instructions
- Verification code input
- Success confirmation screen

### 2. **Enhanced Auth Service** (`lib/services/auth_service.dart`)

Added the following methods:

- `setUpTotp()` - Initialize TOTP setup and get QR code URI
- `verifyTotpSetup(code)` - Verify and enable TOTP
- `confirmSignInWithMfaCode(code)` - Confirm MFA during login (TOTP)
- `confirmSignInWithSmsMfaCode(code)` - Confirm SMS MFA during login
- `updateMfaPreference()` - Update MFA preferences

### 3. **Updated Login Flow** (`lib/screens/login_screen.dart`)

- Detects MFA requirement during sign-in
- Redirects to MFA confirmation screen
- Supports both TOTP and SMS MFA

### 4. **MFA Confirmation Screen** (`lib/screens/mfa_confirmation_screen.dart`)

- Accepts 6-digit MFA codes
- Handles authentication completion
- Stores auth tokens properly

### 5. **Home Screen Integration** (`lib/screens/home_screen.dart`)

- Added "Setup MFA" action card
- Easy access to MFA setup from dashboard

## 🚀 How to Use MFA in Your App

### For Users:

#### **Enabling MFA:**

1. Sign in to your account
2. Go to Dashboard (Home Screen)
3. Tap on "Setup MFA"
4. Download an authenticator app (Google Authenticator, Microsoft Authenticator, or Authy)
5. Scan the QR code or manually enter the secret key
6. Enter the 6-digit verification code
7. MFA is now enabled!

#### **Signing in with MFA:**

1. Enter your email and password
2. You'll be prompted for an MFA code
3. Open your authenticator app
4. Enter the 6-digit code
5. Successfully signed in!

### For Developers:

#### **Testing MFA Flow:**

```dart
// 1. User signs in
final signInResult = await authService.signIn(
  usernameOrEmail: 'user@example.com',
  password: 'password123',
);

// 2. Check if MFA is required
if (signInResult.nextStep.signInStep ==
    AuthSignInStep.confirmSignInWithTotpMfaCode) {
  // Navigate to MFA confirmation screen
}

// 3. Confirm with MFA code
final result = await authService.confirmSignInWithMfaCode(
  code: '123456',
);
```

## 📱 Supported Authenticator Apps

- Google Authenticator (iOS/Android)
- Microsoft Authenticator (iOS/Android)
- Authy (iOS/Android/Desktop)
- 1Password
- Any TOTP-compatible app

## ⚙️ AWS Cognito User Pool Configuration

### Required Settings (Already Configured):

1. **MFA Type**: Optional or Required
2. **MFA Methods**:
   - ✅ TOTP (Time-based One-Time Password)
   - ✅ SMS (Optional)

### To Verify/Update Settings:

1. Go to AWS Console → Cognito → User Pools
2. Select your user pool
3. Go to "Sign-in experience" → "Multi-factor authentication"
4. Ensure MFA is enabled and TOTP is selected
5. Save changes

## 🔧 Code Structure

```
lib/
├── screens/
│   ├── mfa_setup_screen.dart          # NEW: Setup MFA with QR code
│   ├── mfa_confirmation_screen.dart   # EXISTING: Verify MFA during login
│   ├── login_screen.dart              # UPDATED: Handle MFA flow
│   └── home_screen.dart               # UPDATED: Add MFA setup button
└── services/
    └── auth_service.dart              # UPDATED: Added MFA methods
```

## 🛠️ Dependencies Used

```yaml
dependencies:
  amplify_flutter: ^2.0.0
  amplify_auth_cognito: ^2.0.0
  qr_flutter: ^4.1.0 # For QR code generation
```

## 🔐 Security Best Practices

1. **TOTP is more secure than SMS**: We've implemented TOTP as the primary method
2. **QR codes contain sensitive data**: They're only shown during setup
3. **Secret keys are displayed with copy option**: For users who can't scan QR codes
4. **Tokens are stored securely**: Using flutter_secure_storage
5. **MFA is optional by default**: Users can choose to enable it

## 📝 Important Notes

1. **First-time Setup**: Users must set up MFA from the home screen after logging in
2. **Backup Codes**: Consider implementing backup codes for account recovery
3. **SMS MFA**: If you want SMS MFA, configure it in Cognito and add phone verification
4. **Recovery Flow**: Users who lose their authenticator should contact support

## 🐛 Troubleshooting

### "Invalid MFA code"

- Ensure phone/device time is synchronized
- Code expires after 30 seconds
- Each code can only be used once

### "TOTP setup failed"

- User might already have MFA enabled
- Check Cognito user pool settings
- Verify network connectivity

### "QR code not displaying"

- Check that qr_flutter dependency is installed
- Verify the URI is valid
- Check for rendering errors in logs

## 🚀 Next Steps (Optional Enhancements)

1. **Disable MFA Option**: Allow users to disable MFA
2. **Backup Codes**: Generate one-time backup codes
3. **SMS MFA**: Add SMS as an alternative MFA method
4. **Remember Device**: Allow trusted devices to skip MFA
5. **MFA Status Display**: Show whether MFA is enabled in profile

## 📞 Support

If users encounter issues with MFA:

1. Check authenticator app time sync
2. Try manual secret key entry
3. Verify Cognito user pool configuration
4. Contact support for account recovery

---

**Implementation Date**: November 6, 2025  
**Amplify Version**: 2.0.0  
**MFA Type**: TOTP (Time-based One-Time Password)
