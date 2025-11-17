import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../models/leave_balance.dart';
import '../models/leave_request.dart';
import '../models/attendance.dart';

class ApiService {
  // Spring Boot backend (Authentication & User Management)
  static const String authBaseUrl = 'http://192.168.91.13:8080/api/v1';

  // Node.js backend (Leave & Attendance Management)
  static const String leaveAttendanceBaseUrl =
      'http://192.168.91.13:3001/api/v1';

  // Legacy baseUrl for backward compatibility
  static const String baseUrl = authBaseUrl;

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

    print('GET /me - Status: ${response.statusCode}');
    print('GET /me - Body: ${response.body}');

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

  // ============== LEAVE MANAGEMENT ==============

  // Get leave balance for current user
  Future<List<LeaveBalance>> getLeaveBalance() async {
    final token = await _getToken();
    if (token == null) {
      throw Exception('No authentication token found');
    }

    print('Fetching leave balance from Node.js backend');
    final url = '$leaveAttendanceBaseUrl/leave/balance';
    print('URL: $url');

    final response = await http.get(
      Uri.parse(url),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
    );

    print('Response status: ${response.statusCode}');
    print('Response body: ${response.body}');

    if (response.statusCode == 200) {
      try {
        final responseData = json.decode(response.body);
        print('Parsed response data: $responseData');

        // Check if the response has the expected structure
        if (responseData['data'] == null) {
          throw Exception('Response missing "data" field');
        }
        if (responseData['data']['balances'] == null) {
          throw Exception('Response missing "data.balances" field');
        }

        final List<dynamic> balances = responseData['data']['balances'];
        print('Number of balances: ${balances.length}');

        return balances.map((json) => LeaveBalance.fromJson(json)).toList();
      } catch (e) {
        print('Error parsing response: $e');
        rethrow;
      }
    } else if (response.statusCode == 401) {
      await clearToken();
      throw Exception('Unauthorized - please login again');
    } else {
      throw Exception(
          'Failed to load leave balance: ${response.statusCode} - ${response.body}');
    }
  }

  // Apply for leave
  Future<Map<String, dynamic>> applyLeave({
    required String policyId,
    required String startDate,
    required String endDate,
    required double totalDays,
    required bool isHalfDay,
  }) async {
    final token = await _getToken();
    if (token == null) {
      throw Exception('No authentication token found');
    }

    // Get user data to extract user_id
    final userData = await getCurrentUser();
    final userId = userData['id'];

    final leaveRequest = LeaveRequest(
      userId: userId,
      policyId: policyId,
      startDate: startDate,
      endDate: endDate,
      totalDays: totalDays,
      isHalfDay: isHalfDay,
    );

    final response = await http.post(
      Uri.parse('$leaveAttendanceBaseUrl/leave/requests'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(leaveRequest.toJson()),
    );

    if (response.statusCode == 200 || response.statusCode == 201) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      await clearToken();
      throw Exception('Unauthorized - please login again');
    } else {
      final errorBody = json.decode(response.body);
      throw Exception(errorBody['message'] ??
          'Failed to apply leave: ${response.statusCode}');
    }
  }

  // ============== ATTENDANCE ==============

  // Clock in
  Future<Map<String, dynamic>> clockIn({
    double? latitude,
    double? longitude,
    String? location,
  }) async {
    final token = await _getToken();
    if (token == null) {
      throw Exception('No authentication token found');
    }

    // Get user data to extract user_id
    final userData = await getCurrentUser();
    final userId = userData['id'];

    final attendance = AttendanceRecord(
      userId: userId,
      timestamp: DateTime.now(),
      type: 'clock-in',
      latitude: latitude,
      longitude: longitude,
      location: location,
    );

    final response = await http.post(
      Uri.parse('$leaveAttendanceBaseUrl/attendance/clock-in'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(attendance.toJson()),
    );

    if (response.statusCode == 200 || response.statusCode == 201) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      await clearToken();
      throw Exception('Unauthorized - please login again');
    } else {
      final errorBody = json.decode(response.body);
      throw Exception(
          errorBody['message'] ?? 'Failed to clock in: ${response.statusCode}');
    }
  }

  // Clock out
  Future<Map<String, dynamic>> clockOut({
    double? latitude,
    double? longitude,
    String? location,
  }) async {
    final token = await _getToken();
    if (token == null) {
      throw Exception('No authentication token found');
    }

    // Get user data to extract user_id
    final userData = await getCurrentUser();
    final userId = userData['id'];

    final attendance = AttendanceRecord(
      userId: userId,
      timestamp: DateTime.now(),
      type: 'clock-out',
      latitude: latitude,
      longitude: longitude,
      location: location,
    );

    final response = await http.post(
      Uri.parse('$leaveAttendanceBaseUrl/attendance/clock-out'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: json.encode(attendance.toJson()),
    );

    if (response.statusCode == 200 || response.statusCode == 201) {
      return json.decode(response.body);
    } else if (response.statusCode == 401) {
      await clearToken();
      throw Exception('Unauthorized - please login again');
    } else {
      final errorBody = json.decode(response.body);
      throw Exception(errorBody['message'] ??
          'Failed to clock out: ${response.statusCode}');
    }
  }
}
