class AttendanceRecord {
  final String? id;
  final String userId;
  final DateTime timestamp;
  final String type; // 'clock-in' or 'clock-out'
  final double? latitude;
  final double? longitude;
  final String? location;

  AttendanceRecord({
    this.id,
    required this.userId,
    required this.timestamp,
    required this.type,
    this.latitude,
    this.longitude,
    this.location,
  });

  Map<String, dynamic> toJson() {
    final data = <String, dynamic>{
      'user_id': userId,
      'timestamp': timestamp.toIso8601String(),
      'type': type,
    };

    if (latitude != null) data['latitude'] = latitude!;
    if (longitude != null) data['longitude'] = longitude!;
    if (location != null) data['location'] = location!;

    return data;
  }

  factory AttendanceRecord.fromJson(Map<String, dynamic> json) {
    return AttendanceRecord(
      id: json['id'],
      userId: json['user_id'] ?? '',
      timestamp: json['timestamp'] != null
          ? DateTime.parse(json['timestamp'])
          : DateTime.now(),
      type: json['type'] ?? '',
      latitude: json['latitude']?.toDouble(),
      longitude: json['longitude']?.toDouble(),
      location: json['location'],
    );
  }
}
