# Backend Integration Checklist

## Database Configuration

Your new RDS database should have tables for:

### 1. Leave Policies Table

Stores different types of leave (Sick Leave, Annual Leave, etc.)

- policy_id (UUID, Primary Key)
- policy_name (String)
- Other policy configuration fields

### 2. Leave Balances Table

Tracks leave balance for each user and policy

- user_id (String, Foreign Key)
- policy_id (UUID, Foreign Key)
- year (Integer)
- allocated_days (Decimal)
- used_days (Decimal)
- pending_days (Decimal)
- carried_forward (Decimal)

### 3. Leave Requests Table

Stores leave applications

- request_id (UUID, Primary Key)
- user_id (String, Foreign Key)
- policy_id (UUID, Foreign Key)
- start_date (Date)
- end_date (Date)
- total_days (Decimal)
- is_half_day (Boolean)
- status (String: pending/approved/rejected)
- created_at (Timestamp)
- updated_at (Timestamp)

### 4. Attendance Records Table

Stores clock in/out records

- attendance_id (UUID, Primary Key)
- user_id (String, Foreign Key)
- timestamp (Timestamp)
- type (String: 'clock-in' or 'clock-out')
- latitude (Decimal, Optional)
- longitude (Decimal, Optional)
- location (String, Optional)
- created_at (Timestamp)

## API Endpoints to Implement

### 1. GET /api/v1/leave/balance

**Query Parameters:**

- user_id: string (required)

**Response:** Array of leave balance objects

```json
[
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
]
```

**Authentication:** Required (JWT Bearer token)

**Business Logic:**

- Calculate available_days = allocated_days + carried_forward - used_days - pending_days
- Return all active leave policies for the user
- Filter by current year or include year parameter

### 2. POST /api/v1/leave/requests

**Request Body:**

```json
{
  "user_id": "string",
  "policy_id": "uuid",
  "start_date": "YYYY-MM-DD",
  "end_date": "YYYY-MM-DD",
  "total_days": 1.0,
  "is_half_day": false
}
```

**Response:**

```json
{
  "request_id": "uuid",
  "status": "pending",
  "message": "Leave request submitted successfully"
}
```

**Authentication:** Required (JWT Bearer token)

**Validations:**

- Verify user has sufficient leave balance
- Check for overlapping leave requests
- Validate date range (end_date >= start_date)
- Verify policy_id exists and is active
- If is_half_day is true, ensure start_date equals end_date

**Business Logic:**

- Create new leave request with status "pending"
- Update pending_days in leave balance
- Send notification to approver (optional)

### 3. POST /api/v1/attendance/clock-in

**Request Body:**

```json
{
  "user_id": "string",
  "timestamp": "2025-11-14T09:00:00Z",
  "type": "clock-in",
  "latitude": 6.9271,
  "longitude": 79.8612,
  "location": "6.9271, 79.8612"
}
```

**Response:**

```json
{
  "attendance_id": "uuid",
  "message": "Clocked in successfully",
  "timestamp": "2025-11-14T09:00:00Z"
}
```

**Authentication:** Required (JWT Bearer token)

**Validations:**

- Check if user is already clocked in (no duplicate clock-ins)
- Validate timestamp is not in the future
- Location fields are optional

**Business Logic:**

- Create attendance record with type "clock-in"
- Store location data if provided
- Calculate if user is late (optional)

### 4. POST /api/v1/attendance/clock-out

**Request Body:**

```json
{
  "user_id": "string",
  "timestamp": "2025-11-14T17:00:00Z",
  "type": "clock-out",
  "latitude": 6.9271,
  "longitude": 79.8612,
  "location": "6.9271, 79.8612"
}
```

**Response:**

```json
{
  "attendance_id": "uuid",
  "message": "Clocked out successfully",
  "timestamp": "2025-11-14T17:00:00Z",
  "hours_worked": 8.0
}
```

**Authentication:** Required (JWT Bearer token)

**Validations:**

- Verify user has clocked in today
- Ensure no duplicate clock-out for the same clock-in
- Validate timestamp is after clock-in time

**Business Logic:**

- Create attendance record with type "clock-out"
- Calculate hours worked (optional)
- Store location data if provided

## Lambda Functions Configuration

### Environment Variables to Set:

```
DB_HOST=your-rds-endpoint
DB_PORT=5432
DB_NAME=your_database_name
DB_USER=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret
```

### IAM Permissions Required:

- RDS Data Access
- VPC Access (if RDS is in VPC)
- CloudWatch Logs

### API Gateway Configuration:

- Enable CORS for all endpoints
- Add JWT Authorizer
- Set appropriate timeout (30 seconds recommended)
- Enable request validation

## Security Considerations

1. **JWT Token Validation:**

   - Verify token signature
   - Check token expiration
   - Extract user_id from token claims
   - Compare user_id in request body with token user_id

2. **Input Validation:**

   - Sanitize all inputs
   - Validate UUID format for IDs
   - Validate date formats
   - Check coordinate ranges for latitude/longitude

3. **Authorization:**

   - Verify user can only access their own data
   - Implement role-based access if needed
   - Admin users can view all records

4. **Rate Limiting:**
   - Implement rate limiting to prevent abuse
   - Especially important for clock-in/out endpoints

## Testing Endpoints

### Using Postman or cURL:

**Get Leave Balance:**

```bash
curl -X GET \
  'https://your-api-gateway-url/api/v1/leave/balance?user_id=USER_ID' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Apply Leave:**

```bash
curl -X POST \
  'https://your-api-gateway-url/api/v1/leave/requests' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
    "user_id": "USER_ID",
    "policy_id": "POLICY_UUID",
    "start_date": "2025-11-20",
    "end_date": "2025-11-22",
    "total_days": 3.0,
    "is_half_day": false
  }'
```

**Clock In:**

```bash
curl -X POST \
  'https://your-api-gateway-url/api/v1/attendance/clock-in' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
    "user_id": "USER_ID",
    "timestamp": "2025-11-14T09:00:00Z",
    "type": "clock-in",
    "latitude": 6.9271,
    "longitude": 79.8612,
    "location": "6.9271, 79.8612"
  }'
```

## Error Handling

Implement proper HTTP status codes:

- 200: Success
- 201: Created (for POST requests)
- 400: Bad Request (validation errors)
- 401: Unauthorized (invalid/missing token)
- 403: Forbidden (insufficient permissions)
- 404: Not Found (resource doesn't exist)
- 409: Conflict (duplicate records)
- 500: Internal Server Error

Error response format:

```json
{
  "error": "Error message",
  "message": "Detailed error description",
  "code": "ERROR_CODE"
}
```

## Database Indexes

Recommended indexes for performance:

```sql
-- Leave Balances
CREATE INDEX idx_leave_balance_user_year ON leave_balances(user_id, year);
CREATE INDEX idx_leave_balance_policy ON leave_balances(policy_id);

-- Leave Requests
CREATE INDEX idx_leave_requests_user ON leave_requests(user_id);
CREATE INDEX idx_leave_requests_dates ON leave_requests(start_date, end_date);
CREATE INDEX idx_leave_requests_status ON leave_requests(status);

-- Attendance
CREATE INDEX idx_attendance_user_date ON attendance_records(user_id, timestamp);
CREATE INDEX idx_attendance_type ON attendance_records(type);
```

## Next Steps

1. Set up new RDS database with required tables
2. Implement Lambda functions for each endpoint
3. Configure API Gateway with endpoints
4. Test each endpoint individually
5. Update mobile app baseUrl to point to API Gateway
6. Perform end-to-end testing

## Mobile App Configuration

In `lib/services/api_service.dart`, update:

```dart
static const String baseUrl = 'https://your-api-gateway-url/api/v1';
```

Replace with your actual API Gateway URL.
