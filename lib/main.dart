import 'package:flutter/material.dart';
import 'package:amplify_flutter/amplify_flutter.dart';
import 'package:amplify_auth_cognito/amplify_auth_cognito.dart';
import 'amplifyconfiguration.dart';
import 'screens/splash_screen.dart';

/// Perform Amplify configuration before the UI starts to reduce race conditions
/// where widgets attempt auth operations before Amplify is ready.
Future<void> _preConfigureAmplify() async {
  try {
    WidgetsFlutterBinding.ensureInitialized();
    if (!Amplify.isConfigured) {
      await Amplify.addPlugin(AmplifyAuthCognito());
      await Amplify.configure(amplifyconfig);
      safePrint('Amplify pre-configured in main()');
    } else {
      safePrint('Amplify already configured (pre-run)');
    }
  } on Exception catch (e) {
    safePrint('Failed pre-configuring Amplify: $e');
  }
}

Future<void> main() async {
  await _preConfigureAmplify();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool _amplifyConfigured = false;

  @override
  void initState() {
    super.initState();
    _configureAmplify();
  }

  Future<void> _configureAmplify() async {
    try {
      // Check if already configured
      if (Amplify.isConfigured) {
        setState(() {
          _amplifyConfigured = true;
        });
        return;
      }

      // Add Cognito Auth plugin
      await Amplify.addPlugin(AmplifyAuthCognito());

      // Configure Amplify
      await Amplify.configure(amplifyconfig);

      setState(() {
        _amplifyConfigured = true;
      });

      safePrint('Amplify configured successfully');
    } on Exception catch (e) {
      safePrint('Error configuring Amplify: $e');
      // Still allow app to continue (show Splash which will handle unauth state)
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Staff Auth',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: _amplifyConfigured
          ? const SplashScreen()
          : const Scaffold(
              body: Center(
                child: CircularProgressIndicator(),
              ),
            ),
    );
  }
}
