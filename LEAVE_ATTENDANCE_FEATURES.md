# Leave Management & Attendance Features

This document describes the newly implemented Leave Management and Attendance tracking features.

## Features Implemented

### 1. Leave Management

- **View Leave Balance**: Display all leave types with allocated, used, pending, and available days
- **Apply for Leave**: Submit leave requests with date range selection
- **Half-Day Leave**: Support for half-day leave requests

### 2. Attendance Tracking

- **Clock In/Out**: Record attendance with timestamp
- **Optional Geolocation**: Toggle to include/exclude location data with attendance
- **Real-time Clock**: Display current time and date
- **Last Action Tracking**: Show the last clock in/out action with timestamp

## API Endpoints

### Leave Management

- `GET /api/v1/leave/balance?user_id={user_id}` - Get leave balance for user
- `POST /api/v1/leave/requests` - Submit a leave request

### Attendance

- `POST /api/v1/attendance/clock-in` - Record clock in
- `POST /api/v1/attendance/clock-out` - Record clock out

## File Structure

```
lib/
├── models/
│   ├── leave_balance.dart      # Leave balance data model
│   ├── leave_request.dart      # Leave request data model
│   └── attendance.dart          # Attendance record data model
├── screens/
│   ├── leave_screen.dart       # Leave management UI
│   ├── attendance_screen.dart  # Attendance tracking UI
│   └── home_screen.dart        # Updated with navigation
└── services/
    └── api_service.dart        # Extended with new API methods
```

## Dependencies Added

```yaml
geolocator: ^10.1.0 # For location tracking
permission_handler: ^11.0.1 # For handling permissions
intl: ^0.19.0 # For date formatting
```

## Configuration

### Android Permissions

Added to `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### iOS Permissions

Added to `ios/Runner/Info.plist`:

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>This app needs access to your location to record attendance with location data.</string>
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>This app needs access to your location to record attendance with location data.</string>
```

## Usage

### Accessing Features

From the home screen, tap on:

- **Leave Management** - To view balance and apply for leave
- **Attendance** - To clock in/out

### Leave Management Flow

1. Tap "Leave Management" from home screen
2. View Balance tab shows all leave types with balances
3. Switch to "Apply Leave" tab
4. Select leave type from dropdown
5. Choose start and end dates
6. Toggle "Half Day Leave" if needed
7. Review total days
8. Submit request

### Attendance Flow

1. Tap "Attendance" from home screen
2. Toggle "Use Location" to enable/disable geolocation
3. Tap "Clock In" when starting work
4. Tap "Clock Out" when ending work
5. Location will be recorded if enabled and permission granted

## Data Models

### LeaveBalance

```dart
{
  "policy_id": "uuid",
  "policy_name": "Sick Leave",
  "year": 2025,
  "allocated_days": 10,
  "used_days": 2,
  "pending_days": 0,
  "carried_forward": 0,
  "available_days": 8
}
```

### LeaveRequest

```dart
{
  "user_id": "string",
  "policy_id": "uuid",
  "start_date": "YYYY-MM-DD",
  "end_date": "YYYY-MM-DD",
  "total_days": 1.0,
  "is_half_day": false
}
```

### AttendanceRecord

```dart
{
  "user_id": "string",
  "timestamp": "ISO8601 datetime",
  "type": "clock-in" | "clock-out",
  "latitude": 6.9271,      // Optional
  "longitude": 79.8612,    // Optional
  "location": "lat, long"  // Optional
}
```

## Security

- All API calls require authentication via JWT token
- User ID is automatically extracted from the authenticated user's token
- Tokens are stored securely using flutter_secure_storage
- Location data is only collected with user permission

## Testing

### Before Testing

1. Ensure backend API is running and accessible
2. Update `baseUrl` in `lib/services/api_service.dart` if needed
3. Run `flutter pub get` to install dependencies

### Test Leave Management

1. Login to the app
2. Navigate to Leave Management
3. Verify leave balance loads correctly
4. Try applying for leave with different date ranges
5. Test half-day leave option

### Test Attendance

1. Navigate to Attendance screen
2. Test clock in without location
3. Enable location and grant permissions
4. Test clock in with location
5. Test clock out with and without location
6. Verify last action is displayed

## Backend Requirements

Your backend must:

1. Accept the specified request formats
2. Return the specified response formats
3. Validate JWT tokens in Authorization header
4. Handle optional location fields in attendance records

## Troubleshooting

### Location Not Working

- Ensure location permissions are granted in device settings
- Check if location services are enabled
- Verify GPS signal is available

### API Errors

- Check backend URL in api_service.dart
- Verify backend is running and accessible
- Check network connectivity
- Review backend logs for errors

### Build Issues

- Run `flutter clean`
- Run `flutter pub get`
- Rebuild the project

## Future Enhancements

Possible improvements:

- Leave history view
- Attendance history and reports
- Push notifications for leave approval
- Offline support with sync
- Calendar view for leave planning
- Work hours calculation
- Export reports functionality
