class LeaveRequest {
  final String userId;
  final String policyId;
  final String startDate;
  final String endDate;
  final double totalDays;
  final bool isHalfDay;

  LeaveRequest({
    required this.userId,
    required this.policyId,
    required this.startDate,
    required this.endDate,
    required this.totalDays,
    required this.isHalfDay,
  });

  Map<String, dynamic> toJson() {
    return {
      'user_id': userId,
      'policy_id': policyId,
      'start_date': startDate,
      'end_date': endDate,
      'total_days': totalDays,
      'is_half_day': isHalfDay,
    };
  }

  factory LeaveRequest.fromJson(Map<String, dynamic> json) {
    return LeaveRequest(
      userId: json['user_id'] ?? '',
      policyId: json['policy_id'] ?? '',
      startDate: json['start_date'] ?? '',
      endDate: json['end_date'] ?? '',
      totalDays: (json['total_days'] ?? 0).toDouble(),
      isHalfDay: json['is_half_day'] ?? false,
    );
  }
}
