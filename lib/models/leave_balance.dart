class LeaveBalance {
  final String policyId;
  final String policyName;
  final String leaveType;
  final double allocatedDays;
  final double usedDays;
  final double pendingDays;
  final double carriedForwardDays;
  final double accruedDays;
  final double availableDays;
  final double totalEntitlement;

  LeaveBalance({
    required this.policyId,
    required this.policyName,
    required this.leaveType,
    required this.allocatedDays,
    required this.usedDays,
    required this.pendingDays,
    required this.carriedForwardDays,
    required this.accruedDays,
    required this.availableDays,
    required this.totalEntitlement,
  });

  factory LeaveBalance.fromJson(Map<String, dynamic> json) {
    return LeaveBalance(
      policyId: json['policy_id'] ?? '',
      policyName: json['policy_name'] ?? '',
      leaveType: json['leave_type'] ?? '',
      allocatedDays: (json['allocated_days'] ?? 0).toDouble(),
      usedDays: (json['used_days'] ?? 0).toDouble(),
      pendingDays: (json['pending_days'] ?? 0).toDouble(),
      carriedForwardDays: (json['carried_forward_days'] ?? 0).toDouble(),
      accruedDays: (json['accrued_days'] ?? 0).toDouble(),
      availableDays: (json['available_days'] ?? 0).toDouble(),
      totalEntitlement: (json['total_entitlement'] ?? 0).toDouble(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'policy_id': policyId,
      'policy_name': policyName,
      'leave_type': leaveType,
      'allocated_days': allocatedDays,
      'used_days': usedDays,
      'pending_days': pendingDays,
      'carried_forward_days': carriedForwardDays,
      'accrued_days': accruedDays,
      'available_days': availableDays,
      'total_entitlement': totalEntitlement,
    };
  }
}
