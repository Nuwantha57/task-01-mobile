# MFA Quick Test Script

**Quick Reference for Manual Testing**

## 🚀 Quick Start (5 Minutes)

### 1️⃣ Login (No MFA Yet)

```
1. Launch app
2. Email: [your-test-email]
3. Password: [your-password]
4. Tap "Sign In"
5. ✅ Should reach Home Screen
```

### 2️⃣ Setup MFA

```
1. Tap "Setup MFA" card
2. Wait for QR code
3. Open Google Authenticator (or any TOTP app)
4. Scan QR code
5. Enter 6-digit code from app
6. Tap "Verify and Enable MFA"
7. ✅ Should see success screen
8. Tap "Done"
```

### 3️⃣ Test MFA Login

```
1. Logout from app
2. Login with same credentials
3. ✅ Should prompt for MFA code
4. Open authenticator app
5. Enter current 6-digit code
6. Tap "Verify"
7. ✅ Should reach Home Screen
```

---

## 🧪 Quick Error Tests (2 Minutes)

### Test Invalid Code

```
At step 3 of "Test MFA Login":
- Enter "000000" instead of real code
- ✅ Should show error
- ✅ Should allow retry
```

### Test Expired Code

```
At step 3 of "Test MFA Login":
- Wait for code to change in authenticator
- Enter the OLD code
- ✅ Should show error
```

---

## ✅ Checklist (Quick Version)

**Setup Phase:**

- [ ] QR code displays clearly
- [ ] Can scan with authenticator app
- [ ] Can copy secret key manually
- [ ] Valid code verifies successfully
- [ ] Invalid code shows error
- [ ] Success screen appears

**Login Phase:**

- [ ] Password login prompts for MFA
- [ ] Valid MFA code logs in
- [ ] Invalid MFA code shows error
- [ ] Can retry after error

**UI/UX:**

- [ ] No keyboard infinite loops
- [ ] Buttons disable during loading
- [ ] Error messages are clear
- [ ] Navigation works smoothly

---

## 📸 Key Screenshots Needed

1. Home screen with "Setup MFA" button
2. QR code screen
3. Authenticator app with code
4. Success screen
5. MFA login prompt
6. Any errors encountered

---

## 🐛 Common Issues to Check

❌ **Keyboard keeps popping up and closing**

- This was fixed - should NOT happen anymore

❌ **Can't tap verify button**

- Check if button is enabled
- Ensure 6 digits entered

❌ **QR code not scanning**

- Try manual entry instead
- Check camera permissions

❌ **Code not working**

- Ensure code is current (30-second window)
- Check device time is correct

---

## ⏱️ Time Estimates

- Complete setup: 2-3 minutes
- Quick error tests: 1-2 minutes
- Full test suite: 15-20 minutes
- With screenshots: 25-30 minutes

---

## 🎯 Critical Path Test (Must Pass)

```
1. Fresh login ✓
2. Navigate to Setup MFA ✓
3. Scan QR code ✓
4. Enter valid code ✓
5. See success message ✓
6. Logout ✓
7. Login with password ✓
8. Prompted for MFA ✓
9. Enter MFA code ✓
10. Access home screen ✓
```

**If all 10 steps pass → MFA feature is working! ✅**

---

## 📞 Test Support

If you encounter issues:

1. Check Flutter console for errors
2. Review `MFA_MANUAL_TEST_GUIDE.md` for detailed steps
3. Check `MFA_IMPLEMENTATION_GUIDE.md` for technical details
4. Verify AWS Cognito settings
