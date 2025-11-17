import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../services/api_service.dart';
import '../models/leave_balance.dart';

class LeaveScreen extends StatefulWidget {
  const LeaveScreen({super.key});

  @override
  State<LeaveScreen> createState() => _LeaveScreenState();
}

class _LeaveScreenState extends State<LeaveScreen> {
  final ApiService _apiService = ApiService();
  List<LeaveBalance>? _leaveBalances;
  bool _isLoading = true;
  String? _error;
  int _selectedTabIndex = 0;

  @override
  void initState() {
    super.initState();
    _loadLeaveBalances();
  }

  Future<void> _loadLeaveBalances() async {
    setState(() {
      _isLoading = true;
      _error = null;
    });

    try {
      final balances = await _apiService.getLeaveBalance();
      setState(() {
        _leaveBalances = balances;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString().replaceAll('Exception: ', '');
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Leave Management'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loadLeaveBalances,
          ),
        ],
      ),
      body: Column(
        children: [
          // Tab bar
          Container(
            color: Theme.of(context).colorScheme.surfaceVariant,
            child: Row(
              children: [
                Expanded(
                  child: _buildTabButton(
                    'View Balance',
                    Icons.account_balance_wallet,
                    0,
                  ),
                ),
                Expanded(
                  child: _buildTabButton(
                    'Apply Leave',
                    Icons.add_circle_outline,
                    1,
                  ),
                ),
              ],
            ),
          ),
          // Content
          Expanded(
            child: _selectedTabIndex == 0
                ? _buildBalanceView()
                : _buildApplyLeaveView(),
          ),
        ],
      ),
    );
  }

  Widget _buildTabButton(String title, IconData icon, int index) {
    final isSelected = _selectedTabIndex == index;
    return InkWell(
      onTap: () {
        setState(() {
          _selectedTabIndex = index;
        });
      },
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 16),
        decoration: BoxDecoration(
          border: Border(
            bottom: BorderSide(
              color: isSelected
                  ? Theme.of(context).colorScheme.primary
                  : Colors.transparent,
              width: 3,
            ),
          ),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              color: isSelected
                  ? Theme.of(context).colorScheme.primary
                  : Colors.grey,
            ),
            const SizedBox(width: 8),
            Text(
              title,
              style: TextStyle(
                color: isSelected
                    ? Theme.of(context).colorScheme.primary
                    : Colors.grey,
                fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildBalanceView() {
    if (_isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_error != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, size: 64, color: Colors.red),
            const SizedBox(height: 16),
            Text(
              'Error: $_error',
              textAlign: TextAlign.center,
              style: const TextStyle(color: Colors.red),
            ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: _loadLeaveBalances,
              child: const Text('Retry'),
            ),
          ],
        ),
      );
    }

    if (_leaveBalances == null || _leaveBalances!.isEmpty) {
      return const Center(
        child: Text('No leave balance information available'),
      );
    }

    return RefreshIndicator(
      onRefresh: _loadLeaveBalances,
      child: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: _leaveBalances!.length,
        itemBuilder: (context, index) {
          final balance = _leaveBalances![index];
          return _buildBalanceCard(balance);
        },
      ),
    );
  }

  Widget _buildBalanceCard(LeaveBalance balance) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      elevation: 2,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: Text(
                    balance.policyName,
                    style: const TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                Chip(
                  label: Text(balance.leaveType.toUpperCase()),
                  backgroundColor: Colors.blue[100],
                ),
              ],
            ),
            const Divider(height: 24),
            Row(
              children: [
                Expanded(
                  child: _buildBalanceItem(
                    'Available',
                    balance.availableDays,
                    Colors.green,
                    Icons.check_circle,
                  ),
                ),
                Expanded(
                  child: _buildBalanceItem(
                    'Allocated',
                    balance.allocatedDays,
                    Colors.blue,
                    Icons.assignment,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: _buildBalanceItem(
                    'Used',
                    balance.usedDays,
                    Colors.orange,
                    Icons.event_busy,
                  ),
                ),
                Expanded(
                  child: _buildBalanceItem(
                    'Pending',
                    balance.pendingDays,
                    Colors.amber,
                    Icons.pending,
                  ),
                ),
              ],
            ),
            if (balance.carriedForwardDays > 0) ...[
              const SizedBox(height: 12),
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: Colors.purple[50],
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Row(
                  children: [
                    Icon(Icons.arrow_forward, color: Colors.purple[700]),
                    const SizedBox(width: 8),
                    Text(
                      'Carried Forward: ${balance.carriedForwardDays} days',
                      style: TextStyle(
                        color: Colors.purple[700],
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildBalanceItem(
    String label,
    double days,
    Color color,
    IconData icon,
  ) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: color.withOpacity(0.1),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        children: [
          Icon(icon, color: color, size: 28),
          const SizedBox(height: 8),
          Text(
            days.toString(),
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: color,
            ),
          ),
          Text(
            label,
            style: TextStyle(
              fontSize: 12,
              color: color.withOpacity(0.8),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildApplyLeaveView() {
    return _ApplyLeaveForm(
      leaveBalances: _leaveBalances,
      onLeaveApplied: () {
        _loadLeaveBalances();
        setState(() {
          _selectedTabIndex = 0;
        });
      },
    );
  }
}

class _ApplyLeaveForm extends StatefulWidget {
  final List<LeaveBalance>? leaveBalances;
  final VoidCallback onLeaveApplied;

  const _ApplyLeaveForm({
    required this.leaveBalances,
    required this.onLeaveApplied,
  });

  @override
  State<_ApplyLeaveForm> createState() => _ApplyLeaveFormState();
}

class _ApplyLeaveFormState extends State<_ApplyLeaveForm> {
  final _formKey = GlobalKey<FormState>();
  final ApiService _apiService = ApiService();
  final DateFormat _dateFormat = DateFormat('yyyy-MM-dd');

  String? _selectedPolicyId;
  DateTime? _startDate;
  DateTime? _endDate;
  bool _isHalfDay = false;
  bool _isSubmitting = false;

  double get _totalDays {
    if (_startDate == null || _endDate == null) return 0;
    final difference = _endDate!.difference(_startDate!).inDays + 1;
    return _isHalfDay ? 0.5 : difference.toDouble();
  }

  Future<void> _selectDate(BuildContext context, bool isStartDate) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: isStartDate
          ? (_startDate ?? DateTime.now())
          : (_endDate ?? _startDate ?? DateTime.now()),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 365)),
    );

    if (picked != null) {
      setState(() {
        if (isStartDate) {
          _startDate = picked;
          // Reset end date if it's before start date
          if (_endDate != null && _endDate!.isBefore(_startDate!)) {
            _endDate = null;
          }
        } else {
          _endDate = picked;
        }
      });
    }
  }

  Future<void> _submitLeaveRequest() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    if (_selectedPolicyId == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Please select a leave type'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    if (_startDate == null || _endDate == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Please select start and end dates'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    setState(() {
      _isSubmitting = true;
    });

    try {
      await _apiService.applyLeave(
        policyId: _selectedPolicyId!,
        startDate: _dateFormat.format(_startDate!),
        endDate: _dateFormat.format(_endDate!),
        totalDays: _totalDays,
        isHalfDay: _isHalfDay,
      );

      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Leave request submitted successfully!'),
          backgroundColor: Colors.green,
        ),
      );

      // Reset form
      setState(() {
        _selectedPolicyId = null;
        _startDate = null;
        _endDate = null;
        _isHalfDay = false;
      });

      widget.onLeaveApplied();
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
              'Failed to apply leave: ${e.toString().replaceAll('Exception: ', '')}'),
          backgroundColor: Colors.red,
        ),
      );
    } finally {
      if (mounted) {
        setState(() {
          _isSubmitting = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (widget.leaveBalances == null || widget.leaveBalances!.isEmpty) {
      return const Center(
        child: Padding(
          padding: EdgeInsets.all(16.0),
          child: Text(
            'Please load your leave balance first from the View Balance tab',
            textAlign: TextAlign.center,
            style: TextStyle(fontSize: 16),
          ),
        ),
      );
    }

    return Form(
      key: _formKey,
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const Text(
              'Apply for Leave',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 24),

            // Leave Type Dropdown
            DropdownButtonFormField<String>(
              value: _selectedPolicyId,
              decoration: const InputDecoration(
                labelText: 'Leave Type',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.category),
              ),
              items: widget.leaveBalances!.map((balance) {
                return DropdownMenuItem(
                  value: balance.policyId,
                  child: Text(
                    '${balance.policyName} (${balance.availableDays} days available)',
                  ),
                );
              }).toList(),
              onChanged: (value) {
                setState(() {
                  _selectedPolicyId = value;
                });
              },
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please select a leave type';
                }
                return null;
              },
            ),
            const SizedBox(height: 16),

            // Start Date
            InkWell(
              onTap: () => _selectDate(context, true),
              child: InputDecorator(
                decoration: const InputDecoration(
                  labelText: 'Start Date',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.calendar_today),
                ),
                child: Text(
                  _startDate != null
                      ? _dateFormat.format(_startDate!)
                      : 'Select start date',
                  style: TextStyle(
                    color: _startDate != null ? Colors.black : Colors.grey,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),

            // End Date
            InkWell(
              onTap: () => _selectDate(context, false),
              child: InputDecorator(
                decoration: const InputDecoration(
                  labelText: 'End Date',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.event),
                ),
                child: Text(
                  _endDate != null
                      ? _dateFormat.format(_endDate!)
                      : 'Select end date',
                  style: TextStyle(
                    color: _endDate != null ? Colors.black : Colors.grey,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Half Day Switch
            SwitchListTile(
              title: const Text('Half Day Leave'),
              subtitle: const Text('Request only half day leave'),
              value: _isHalfDay,
              onChanged: (value) {
                setState(() {
                  _isHalfDay = value;
                });
              },
            ),
            const SizedBox(height: 16),

            // Total Days Display
            if (_startDate != null && _endDate != null)
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.blue[50],
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(color: Colors.blue[200]!),
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'Total Days:',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    Text(
                      _totalDays.toString(),
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Colors.blue[700],
                      ),
                    ),
                  ],
                ),
              ),
            const SizedBox(height: 24),

            // Submit Button
            ElevatedButton(
              onPressed: _isSubmitting ? null : _submitLeaveRequest,
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 16),
              ),
              child: _isSubmitting
                  ? const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Text(
                      'Submit Leave Request',
                      style: TextStyle(fontSize: 16),
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
