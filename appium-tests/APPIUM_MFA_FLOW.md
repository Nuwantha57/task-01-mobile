# Appium MFA Testing - Complete Flow Documentation

## MFA Implementation Flow (Actual Implementation)

### Flow 1: MFA Setup (First-Time User)

```
1. Login Screen
   ↓ Enter email + password
   ↓ Click "Sign In"
2. Home Screen (if no MFA required)
   ↓ User sees "Setup MFA" card
   ↓ Click "Setup MFA"
3. MFA Setup Screen
   ↓ QR code generated
   ↓ Secret key displayed
   ↓ User scans QR or copies secret
   ↓ Enter 6-digit TOTP code
   ↓ Click "Verify and Enable MFA"
4. Success Screen
   ↓ "MFA Successfully Enabled!"
   ↓ Click "Done"
5. Back to Home Screen
```

### Flow 2: Login with MFA Already Enabled

```
1. Login Screen
   ↓ Enter email + password
   ↓ Click "Sign In"
2. MFA Confirmation Screen (NOT Home!)
   ↓ "Enter MFA Code"
   ↓ Enter 6-digit TOTP code
   ↓ Click "Verify"
3. Home Screen
```

## Appium Test Strategy

### Test Suite Structure

#### Suite 1: MFA Setup Tests

1. **testNavigateToMfaSetup** - Verify navigation to MFA setup
2. **testQrCodeGeneration** - Verify QR code and secret key display
3. **testSecretKeyCopy** - Verify secret key can be copied
4. **testInvalidCodeSetup** - Test error handling with invalid code
5. **testValidCodeSetup** - Complete MFA setup with valid TOTP code
6. **testSuccessScreen** - Verify success screen and navigation

#### Suite 2: MFA Login Tests

7. **testLoginWithMfaPrompt** - Verify MFA prompt appears after password
8. **testValidMfaLogin** - Login successfully with valid TOTP code
9. **testInvalidMfaLogin** - Test error handling with invalid MFA code
10. **testExpiredMfaCode** - Test expired code handling

#### Suite 3: Edge Cases

11. **testBackButtonBehavior** - Test back button during MFA flows
12. **testLogoutAfterMfaSetup** - Verify logout works after MFA setup
13. **testMultipleMfaAttempts** - Test retry mechanism

## Key Test Data Required

### User Types

1. **Fresh User** (no MFA): For setup tests

   - Email: nuwanthapiumal57@gmail.com
   - Password: Nuwantha@1234

2. **MFA-Enabled User**: For login tests
   - Email: nuwanthapiumal57+test3@gmail.com
   - Password: Udara@1234
   - Secret: OWYX4Z2VJCYOIBKVREBZGLWCPAYDMNWRQ27RWIU2ZZ2XPSX4LMTQ

## Critical UI Elements to Identify

### Login Screen

- Email field: `//android.widget.EditText` (hint or label contains "Email")
- Password field: `//android.widget.EditText` (hint or label contains "Password")
- Sign In button: `//android.widget.Button[contains(@text, 'Sign In')]`

### Home Screen

- Title: "Dashboard"
- Setup MFA card: Contains text "Setup MFA"
- Logout button: IconButton with logout icon

### MFA Setup Screen

- AppBar title: "Setup MFA"
- QR Code: `android.widget.ImageView`
- Secret key: SelectableText (monospace)
- Verification input: `//android.widget.EditText` (hint: "123456")
- Verify button: `//android.widget.Button[contains(@text, 'Verify and Enable MFA')]`

### MFA Success Screen

- Check icon: Green check circle
- Title: "MFA Successfully Enabled!"
- Done button: `//android.widget.Button[@text='Done']`

### MFA Confirmation Screen (Login)

- AppBar title: "MFA Verification"
- Security icon
- Title: "Enter MFA Code"
- Input field: `//android.widget.EditText` (hint: "123456")
- Verify button: `//android.widget.Button[@text='Verify']`

## Test Execution Order

### Phase 1: Setup (User without MFA)

```
testNavigateToMfaSetup()
testQrCodeGeneration()
testSecretKeyCopy()
testInvalidCodeSetup()
testValidCodeSetup()
testSuccessScreen()
```

### Phase 2: Login (User with MFA)

```
testLogout()
testLoginWithMfaPrompt()
testInvalidMfaLogin()
testValidMfaLogin()
```

### Phase 3: Edge Cases

```
testExpiredCode()
testBackButton()
testMultipleAttempts()
```

## Assertions Checklist

### MFA Setup Screen

- [ ] QR code is visible
- [ ] Secret key is displayed and non-empty
- [ ] All 3 steps are visible
- [ ] Verification input accepts 6 digits
- [ ] Copy button works
- [ ] Invalid code shows error
- [ ] Valid code shows success
- [ ] Done button navigates back

### MFA Login Screen

- [ ] Appears after password login
- [ ] Shows correct title "Enter MFA Code"
- [ ] Input field is visible
- [ ] Verify button is clickable
- [ ] Invalid code shows error
- [ ] Valid code navigates to Home
- [ ] Error allows retry

## Common Pitfalls to Avoid

1. **Timing Issues**: Allow sufficient wait time for:

   - QR code generation (2-3 seconds)
   - TOTP verification (network call)
   - Navigation transitions

2. **Code Expiration**: TOTP codes expire every 30 seconds

   - Generate code just before entering
   - Don't reuse codes from previous tests

3. **Element Identification**: Flutter apps may have dynamic IDs

   - Use text-based or accessibility-based selectors
   - Verify elements are visible before interaction

4. **State Management**:

   - Ensure clean state between tests
   - Logout before testing login flow
   - Use fresh user for setup tests

5. **Secret Key Extraction**:
   - Parse from QR code URI or read from screen
   - Store for subsequent tests
   - Validate format (Base32, uppercase)

## Expected Test Results

### All Tests Pass Criteria

✅ User can setup MFA successfully
✅ QR code and secret key are displayed
✅ Invalid codes show appropriate errors
✅ Valid TOTP codes are accepted
✅ MFA prompt appears on login
✅ Login succeeds with valid MFA code
✅ Error handling works correctly
✅ Navigation flow is correct
✅ No crashes or hangs

## Running the Tests

```bash
# Start Appium server
appium

# In another terminal, run tests
cd appium-tests
mvn clean test

# Run specific test
mvn test -Dtest=MfaTest#testValidCodeSetup

# Run with verbose output
mvn test -X
```

## Success Metrics

- **Test Coverage**: 100% of MFA user journeys
- **Pass Rate**: 100% on stable environment
- **Execution Time**: < 5 minutes for full suite
- **Flakiness**: 0% (no random failures)
