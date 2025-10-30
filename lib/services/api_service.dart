import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ApiService {
  // CHANGE THIS to your backend URL
  static const String baseUrl =
      'http://192.168.91.13:8080/api/v1'; // Android emulator localhost
  // Use 'http://localhost:8080/api/v1' for iOS simulator
  // Use actual IP for physical device: 'http://192.168.x.x:8080/api/v1'

  final _storage = const FlutterSecureStorage();

  Future<String?> _getToken() async {
    return await _storage.read(key: 'id_token');
  }

  Future<void> _saveToken(String token) async {
    await _storage.write(key: 'id_token', value: token);
  }

  Future<void> clearToken() async {
    await _storage.delete(key: 'id_token');
  }

  // Store Cognito token after login
  Future<void> storeToken(String token) async {
    await _saveToken(token);
  }

  // Get current user profile
  Future<Map<String, dynamic>> getCurrentUser() async {
    final token = await _getToken();
    if (token == null) {
      throw Exception('No authentication token found');
    }

    final response = await http.get(
      Uri.parse('$baseUrl/me'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      await clearToken();
      throw Exception('Unauthorized - please login again');
    } else {
      throw Exception('Failed to load user: ${response.statusCode}');
    }
  }

  // Update user profile
  Future<void> updateProfile({String? displayName, String? locale}) async {
    final token = await _getToken();
    if (token == null) {
      throw Exception('No authentication token found');
    }

    final body = <String, dynamic>{};
    if (displayName != null) body['displayName'] = displayName;
    if (locale != null) body['locale'] = locale;

    final response = await http.patch(
      Uri.parse('$baseUrl/me'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(body),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to update profile: ${response.statusCode}');
    }
  }

  // Logout (backend endpoint)
  Future<void> logout() async {
    final token = await _getToken();
    if (token != null) {
      try {
        await http.post(
          Uri.parse('$baseUrl/sessions/logout'),
          headers: {
            'Authorization': 'Bearer $token',
            'Content-Type': 'application/json',
          },
        );
      } catch (e) {
        print('Logout API error: $e');
      }
    }
    await clearToken();
  }

  // Health check
  Future<bool> healthCheck() async {
    try {
      final response = await http.get(
        Uri.parse('${baseUrl.replaceAll('/api/v1', '')}/healthz'),
      );
      return response.statusCode == 200;
    } catch (e) {
      return false;
    }
  }
}
