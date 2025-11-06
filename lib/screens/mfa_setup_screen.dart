import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:qr_flutter/qr_flutter.dart';
import '../services/auth_service.dart';

class MfaSetupScreen extends StatefulWidget {
  const MfaSetupScreen({super.key});

  @override
  State<MfaSetupScreen> createState() => _MfaSetupScreenState();
}

class _MfaSetupScreenState extends State<MfaSetupScreen> {
  final AuthService _authService = AuthService();
  final _codeController = TextEditingController();

  String? _qrCodeUri;
  String? _secretKey;
  bool _isLoading = true;
  bool _isVerifying = false;
  bool _setupComplete = false;

  @override
  void initState() {
    super.initState();
    _initializeMfaSetup();
  }

  @override
  void dispose() {
    _codeController.dispose();
    super.dispose();
  }

  Future<void> _initializeMfaSetup() async {
    setState(() => _isLoading = true);

    try {
      final uri = await _authService.setUpTotp();

      // Extract secret key from URI for manual entry option
      final uriString = uri.toString();
      final secretMatch = RegExp(r'secret=([A-Z0-9]+)').firstMatch(uriString);

      setState(() {
        _qrCodeUri = uriString;
        _secretKey = secretMatch?.group(1);
        _isLoading = false;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() => _isLoading = false);
      _showError(e.toString().replaceAll('Exception: ', ''));
    }
  }

  Future<void> _verifyAndEnableMfa() async {
    if (_codeController.text.isEmpty || _codeController.text.length != 6) {
      _showError('Please enter a valid 6-digit code');
      return;
    }

    setState(() => _isVerifying = true);

    try {
      // Verify TOTP setup - this automatically enables MFA
      await _authService.verifyTotpSetup(code: _codeController.text.trim());

      if (!mounted) return;

      setState(() {
        _setupComplete = true;
        _isVerifying = false;
      });

      _showSuccess('MFA has been successfully enabled for your account!');
    } catch (e) {
      if (!mounted) return;
      setState(() => _isVerifying = false);
      _showError(e.toString().replaceAll('Exception: ', ''));
    }
  }

  void _copySecretKey() {
    if (_secretKey != null) {
      Clipboard.setData(ClipboardData(text: _secretKey!));
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Secret key copied to clipboard'),
          duration: Duration(seconds: 2),
        ),
      );
    }
  }

  void _showError(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: Colors.red,
      ),
    );
  }

  void _showSuccess(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: Colors.green,
        duration: const Duration(seconds: 3),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Setup MFA'),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _setupComplete
              ? _buildSuccessView()
              : _buildSetupView(),
    );
  }

  Widget _buildSetupView() {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(24.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          const Icon(
            Icons.security,
            size: 60,
            color: Colors.blue,
          ),
          const SizedBox(height: 16),
          const Text(
            'Enable Two-Factor Authentication',
            style: TextStyle(
              fontSize: 22,
              fontWeight: FontWeight.bold,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 8),
          const Text(
            'Scan the QR code with your authenticator app',
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey,
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 24),

          // Step 1: Install Authenticator App
          _buildStep(
            stepNumber: '1',
            title: 'Install an Authenticator App',
            description:
                'Download Google Authenticator, Microsoft Authenticator, or Authy',
          ),
          const SizedBox(height: 16),

          // Step 2: Scan QR Code
          _buildStep(
            stepNumber: '2',
            title: 'Scan QR Code',
            description: 'Open your authenticator app and scan the code below',
          ),
          const SizedBox(height: 12),

          if (_qrCodeUri != null)
            Center(
              child: Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.grey.withOpacity(0.3),
                      spreadRadius: 2,
                      blurRadius: 8,
                    ),
                  ],
                ),
                child: QrImageView(
                  data: _qrCodeUri!,
                  version: QrVersions.auto,
                  size: 200.0,
                  backgroundColor: Colors.white,
                ),
              ),
            ),

          const SizedBox(height: 12),

          // Manual entry option
          if (_secretKey != null)
            Card(
              child: Padding(
                padding: const EdgeInsets.all(12.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      'Can\'t scan? Enter manually:',
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 13,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        Expanded(
                          child: SelectableText(
                            _secretKey!,
                            style: const TextStyle(
                              fontFamily: 'monospace',
                              fontSize: 14,
                            ),
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.copy, size: 20),
                          onPressed: _copySecretKey,
                          tooltip: 'Copy secret key',
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),

          const SizedBox(height: 16),

          // Step 3: Verify
          _buildStep(
            stepNumber: '3',
            title: 'Enter Verification Code',
            description: 'Enter the 6-digit code from your authenticator app',
          ),
          const SizedBox(height: 12),

          TextField(
            controller: _codeController,
            keyboardType: TextInputType.number,
            textAlign: TextAlign.center,
            maxLength: 6,
            style: const TextStyle(
              fontSize: 24,
              letterSpacing: 8,
            ),
            decoration: const InputDecoration(
              labelText: 'Verification Code',
              border: OutlineInputBorder(),
              hintText: '123456',
              counterText: '',
            ),
          ),
          const SizedBox(height: 20),

          ElevatedButton(
            onPressed: _isVerifying ? null : _verifyAndEnableMfa,
            style: ElevatedButton.styleFrom(
              padding: const EdgeInsets.symmetric(vertical: 16),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
              ),
            ),
            child: _isVerifying
                ? const SizedBox(
                    height: 20,
                    width: 20,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                      color: Colors.white,
                    ),
                  )
                : const Text(
                    'Verify and Enable MFA',
                    style: TextStyle(fontSize: 16),
                  ),
          ),
          const SizedBox(height: 24), // Extra bottom padding for scroll
        ],
      ),
    );
  }

  Widget _buildSuccessView() {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.check_circle,
              size: 100,
              color: Colors.green,
            ),
            const SizedBox(height: 24),
            const Text(
              'MFA Successfully Enabled!',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 16),
            const Text(
              'Your account is now protected with two-factor authentication. You\'ll need to enter a code from your authenticator app each time you sign in.',
              style: TextStyle(
                fontSize: 16,
                color: Colors.grey,
              ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 32),
            ElevatedButton(
              onPressed: () => Navigator.pop(context, true),
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(
                  horizontal: 32,
                  vertical: 16,
                ),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              child: const Text(
                'Done',
                style: TextStyle(fontSize: 16),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildStep({
    required String stepNumber,
    required String title,
    required String description,
  }) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: 28,
          height: 28,
          decoration: BoxDecoration(
            color: Colors.blue,
            borderRadius: BorderRadius.circular(14),
          ),
          child: Center(
            child: Text(
              stepNumber,
              style: const TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
                fontSize: 14,
              ),
            ),
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                title,
                style: const TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 2),
              Text(
                description,
                style: TextStyle(
                  fontSize: 13,
                  color: Colors.grey[600],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
