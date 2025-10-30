import 'package:amplify_flutter/amplify_flutter.dart';
import 'package:amplify_auth_cognito/amplify_auth_cognito.dart';
import 'api_service.dart';

class AuthService {
  final ApiService _apiService = ApiService();

  // Check if user is signed in
  Future<bool> isSignedIn() async {
    try {
      final session = await Amplify.Auth.fetchAuthSession();
      return session.isSignedIn;
    } on AuthException catch (e) {
      throw Exception('Error checking sign in status: ${e.message}');
    } catch (e) {
      throw Exception('Unexpected error checking sign in status: $e');
    }
  }

  // ✅ Sign up new user
  Future<SignUpResult> signUp({
    required String username, // non-email username
    required String password,
    required String name,
    required String email,
    required String phoneNumber,
  }) async {
    try {
      final userAttributes = {
        CognitoUserAttributeKey.preferredUsername: username,
        CognitoUserAttributeKey.name: name,
        CognitoUserAttributeKey.email: email,
        CognitoUserAttributeKey.phoneNumber: phoneNumber,
      };

      final result = await Amplify.Auth.signUp(
        username: username,
        password: password,
        options: SignUpOptions(
          userAttributes: userAttributes,
        ),
      );

      return result;
    } on AuthException catch (e) {
      throw Exception('Sign up failed: ${e.message}');
    } catch (e) {
      throw Exception('Unexpected sign up error: $e');
    }
  }

  // Confirm sign up with verification code
  Future<SignUpResult> confirmSignUp({
    required String username,
    required String confirmationCode,
  }) async {
    try {
      final result = await Amplify.Auth.confirmSignUp(
        username: username,
        confirmationCode: confirmationCode,
      );
      return result;
    } on AuthException catch (e) {
      throw Exception('Confirmation failed: ${e.message}');
    }
  }

  // Resend confirmation code
  Future<void> resendSignUpCode({required String username}) async {
    try {
      await Amplify.Auth.resendSignUpCode(username: username);
    } on AuthException catch (e) {
      throw Exception('Failed to resend code: ${e.message}');
    }
  }

  // ✅ Sign in (supports username or email alias)
  Future<SignInResult> signIn({
    required String usernameOrEmail,
    required String password,
  }) async {
    try {
      final result = await Amplify.Auth.signIn(
        username: usernameOrEmail,
        password: password,
      );

      // After successful sign in, get the ID token and store it
      if (result.isSignedIn) {
        final session = await Amplify.Auth.fetchAuthSession(
          options: const FetchAuthSessionOptions(forceRefresh: true),
        ) as CognitoAuthSession;

        final idToken = session.userPoolTokensResult.value.idToken.raw;
        await _apiService.storeToken(idToken);
      }

      return result;
    } on AuthException catch (e) {
      throw Exception('Sign in failed: ${e.message}');
    }
  }

  // Sign out
  Future<void> signOut() async {
    try {
      await _apiService.logout();
      await Amplify.Auth.signOut();
    } on AuthException catch (e) {
      throw Exception('Sign out failed: ${e.message}');
    }
  }

  // Reset password - send code
  Future<void> resetPassword({required String usernameOrEmail}) async {
    try {
      await Amplify.Auth.resetPassword(username: usernameOrEmail);
    } on AuthException catch (e) {
      throw Exception('Password reset failed: ${e.message}');
    }
  }

  // Confirm password reset
  Future<void> confirmResetPassword({
    required String usernameOrEmail,
    required String newPassword,
    required String confirmationCode,
  }) async {
    try {
      await Amplify.Auth.confirmResetPassword(
        username: usernameOrEmail,
        newPassword: newPassword,
        confirmationCode: confirmationCode,
      );
    } on AuthException catch (e) {
      throw Exception('Password confirmation failed: ${e.message}');
    }
  }

  // Get current user attributes
  Future<List<AuthUserAttribute>> getCurrentUserAttributes() async {
    try {
      final attributes = await Amplify.Auth.fetchUserAttributes();
      return attributes;
    } on AuthException catch (e) {
      throw Exception('Failed to fetch user attributes: ${e.message}');
    }
  }

  // Setup MFA (TOTP)
  Future<Uri> setUpTotp() async {
    try {
      final result = await Amplify.Auth.setUpTotp();
      return result.getSetupUri(appName: "StaffAuth");
    } on AuthException catch (e) {
      throw Exception('TOTP setup failed: ${e.message}');
    }
  }

  // Verify TOTP setup
  Future<void> verifyTotpSetup({required String code}) async {
    try {
      await Amplify.Auth.verifyTotpSetup(code);
    } on AuthException catch (e) {
      throw Exception('TOTP verification failed: ${e.message}');
    }
  }

  // Confirm sign in with MFA code
  Future<SignInResult> confirmSignInWithMfaCode({
    required String code,
  }) async {
    try {
      final result = await Amplify.Auth.confirmSignIn(
        confirmationValue: code,
      );
      return result;
    } on AuthException catch (e) {
      throw Exception('MFA confirmation failed: ${e.message}');
    }
  }

  // Get ID Token
  Future<String?> getIdToken() async {
    try {
      final session = await Amplify.Auth.fetchAuthSession(
        options: const FetchAuthSessionOptions(forceRefresh: true),
      ) as CognitoAuthSession;

      return session.userPoolTokensResult.value.idToken.raw;
    } on AuthException catch (e) {
      throw Exception('Error getting ID token: ${e.message}');
    } catch (e) {
      throw Exception('Unexpected error getting ID token: $e');
    }
  }
}
