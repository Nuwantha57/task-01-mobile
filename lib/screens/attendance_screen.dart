import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:intl/intl.dart';
import '../services/api_service.dart';
import '../models/attendance.dart';

class AttendanceScreen extends StatefulWidget {
  const AttendanceScreen({super.key});

  @override
  State<AttendanceScreen> createState() => _AttendanceScreenState();
}

class _AttendanceScreenState extends State<AttendanceScreen> {
  final ApiService _apiService = ApiService();
  bool _isProcessing = false;
  bool _useLocation = true;
  Position? _currentPosition;
  String? _lastAction;
  DateTime? _lastActionTime;
  List<AttendanceRecord> _attendanceRecords = [];
  bool _isLoadingRecords = false;

  @override
  void initState() {
    super.initState();
    _loadAttendanceRecords();
  }

  Future<void> _loadAttendanceRecords() async {
    setState(() {
      _isLoadingRecords = true;
    });

    try {
      final records = await _apiService.getAttendanceRecords();
      if (mounted) {
        setState(() {
          _attendanceRecords = records;
        });
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(
              'Failed to load attendance records: ${e.toString().replaceAll('Exception: ', '')}',
            ),
            backgroundColor: Colors.orange,
          ),
        );
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoadingRecords = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Attendance'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Current Time Display
            Card(
              elevation: 4,
              child: Padding(
                padding: const EdgeInsets.all(24),
                child: Column(
                  children: [
                    const Icon(
                      Icons.access_time,
                      size: 64,
                      color: Colors.blue,
                    ),
                    const SizedBox(height: 16),
                    StreamBuilder(
                      stream: Stream.periodic(const Duration(seconds: 1)),
                      builder: (context, snapshot) {
                        return Text(
                          DateFormat('hh:mm:ss a').format(DateTime.now()),
                          style: const TextStyle(
                            fontSize: 32,
                            fontWeight: FontWeight.bold,
                          ),
                        );
                      },
                    ),
                    const SizedBox(height: 8),
                    Text(
                      DateFormat('EEEE, MMMM d, yyyy').format(DateTime.now()),
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.grey[600],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),

            // Location Toggle
            Card(
              child: SwitchListTile(
                title: const Text('Use Location'),
                subtitle: Text(
                  _useLocation
                      ? 'Location will be recorded with attendance'
                      : 'Location will not be recorded',
                ),
                secondary: Icon(
                  _useLocation ? Icons.location_on : Icons.location_off,
                  color: _useLocation ? Colors.green : Colors.grey,
                ),
                value: _useLocation,
                onChanged: (value) {
                  setState(() {
                    _useLocation = value;
                    if (!value) {
                      _currentPosition = null;
                    }
                  });
                },
              ),
            ),
            const SizedBox(height: 24),

            // Last Action Info
            if (_lastAction != null && _lastActionTime != null)
              Card(
                color: _lastAction == 'Clock In'
                    ? Colors.green[50]
                    : Colors.orange[50],
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Row(
                    children: [
                      Icon(
                        _lastAction == 'Clock In' ? Icons.login : Icons.logout,
                        color: _lastAction == 'Clock In'
                            ? Colors.green
                            : Colors.orange,
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              'Last Action: $_lastAction',
                              style: const TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 16,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              DateFormat('MMM d, yyyy • hh:mm a')
                                  .format(_lastActionTime!),
                              style: TextStyle(
                                color: Colors.grey[600],
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            if (_lastAction != null && _lastActionTime != null)
              const SizedBox(height: 24),

            // Clock In Button
            ElevatedButton.icon(
              onPressed: _isProcessing ? null : () => _handleClockIn(),
              icon: _isProcessing
                  ? const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        color: Colors.white,
                      ),
                    )
                  : const Icon(Icons.login, size: 28),
              label: const Text(
                'Clock In',
                style: TextStyle(fontSize: 18),
              ),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.green,
                foregroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(vertical: 20),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Clock Out Button
            ElevatedButton.icon(
              onPressed: _isProcessing ? null : () => _handleClockOut(),
              icon: _isProcessing
                  ? const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        color: Colors.white,
                      ),
                    )
                  : const Icon(Icons.logout, size: 28),
              label: const Text(
                'Clock Out',
                style: TextStyle(fontSize: 18),
              ),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.orange,
                foregroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(vertical: 20),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
              ),
            ),
            const SizedBox(height: 24),

            // Location Info
            if (_currentPosition != null)
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Row(
                        children: [
                          Icon(Icons.location_on, color: Colors.blue),
                          SizedBox(width: 8),
                          Text(
                            'Current Location',
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 16,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Text('Latitude: ${_currentPosition!.latitude}'),
                      Text('Longitude: ${_currentPosition!.longitude}'),
                      Text(
                          'Accuracy: ${_currentPosition!.accuracy.toStringAsFixed(2)}m'),
                    ],
                  ),
                ),
              ),
            const SizedBox(height: 24),

            // Attendance Records Section
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Attendance History',
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 18,
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.refresh),
                          onPressed:
                              _isLoadingRecords ? null : _loadAttendanceRecords,
                          tooltip: 'Refresh',
                        ),
                      ],
                    ),
                    const SizedBox(height: 16),
                    _isLoadingRecords
                        ? const Center(
                            child: Padding(
                              padding: EdgeInsets.all(16.0),
                              child: CircularProgressIndicator(),
                            ),
                          )
                        : _attendanceRecords.isEmpty
                            ? const Padding(
                                padding: EdgeInsets.all(16.0),
                                child: Center(
                                  child: Text(
                                    'No attendance records found',
                                    style: TextStyle(color: Colors.grey),
                                  ),
                                ),
                              )
                            : SingleChildScrollView(
                                scrollDirection: Axis.horizontal,
                                child: DataTable(
                                  columns: const [
                                    DataColumn(label: Text('Date')),
                                    DataColumn(label: Text('Time')),
                                    DataColumn(label: Text('Type')),
                                    DataColumn(label: Text('Location')),
                                  ],
                                  rows: _attendanceRecords.map((record) {
                                    return DataRow(
                                      cells: [
                                        DataCell(
                                          Text(
                                            DateFormat('MMM d, yyyy')
                                                .format(record.timestamp),
                                          ),
                                        ),
                                        DataCell(
                                          Text(
                                            DateFormat('hh:mm a')
                                                .format(record.timestamp),
                                          ),
                                        ),
                                        DataCell(
                                          Container(
                                            padding: const EdgeInsets.symmetric(
                                              horizontal: 8,
                                              vertical: 4,
                                            ),
                                            decoration: BoxDecoration(
                                              color: record.type == 'clock-in'
                                                  ? Colors.green[100]
                                                  : Colors.orange[100],
                                              borderRadius:
                                                  BorderRadius.circular(12),
                                            ),
                                            child: Text(
                                              record.type == 'clock-in'
                                                  ? 'Clock In'
                                                  : 'Clock Out',
                                              style: TextStyle(
                                                color: record.type == 'clock-in'
                                                    ? Colors.green[900]
                                                    : Colors.orange[900],
                                                fontWeight: FontWeight.bold,
                                                fontSize: 12,
                                              ),
                                            ),
                                          ),
                                        ),
                                        DataCell(
                                          Text(
                                            record.location ?? 'N/A',
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                        ),
                                      ],
                                    );
                                  }).toList(),
                                ),
                              ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<Position?> _getCurrentLocation() async {
    try {
      // Check if location services are enabled
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        if (!mounted) return null;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content:
                Text('Location services are disabled. Please enable them.'),
            backgroundColor: Colors.orange,
          ),
        );
        return null;
      }

      // Check location permission
      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          if (!mounted) return null;
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Location permission denied'),
              backgroundColor: Colors.red,
            ),
          );
          return null;
        }
      }

      if (permission == LocationPermission.deniedForever) {
        if (!mounted) return null;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text(
              'Location permission permanently denied. Please enable in settings.',
            ),
            backgroundColor: Colors.red,
          ),
        );
        return null;
      }

      // Get current position
      Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );

      return position;
    } catch (e) {
      if (!mounted) return null;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Error getting location: $e'),
          backgroundColor: Colors.red,
        ),
      );
      return null;
    }
  }

  Future<void> _handleClockIn() async {
    setState(() {
      _isProcessing = true;
    });

    Position? position;
    if (_useLocation) {
      position = await _getCurrentLocation();
      if (position == null && _useLocation) {
        setState(() {
          _isProcessing = false;
        });
        return;
      }
      setState(() {
        _currentPosition = position;
      });
    }

    try {
      await _apiService.clockIn(
        latitude: position?.latitude,
        longitude: position?.longitude,
        location: position != null
            ? '${position.latitude}, ${position.longitude}'
            : null,
      );

      if (!mounted) return;

      setState(() {
        _lastAction = 'Clock In';
        _lastActionTime = DateTime.now();
      });

      // Refresh attendance records
      await _loadAttendanceRecords();

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Clocked in successfully!'),
          backgroundColor: Colors.green,
        ),
      );
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            'Failed to clock in: ${e.toString().replaceAll('Exception: ', '')}',
          ),
          backgroundColor: Colors.red,
        ),
      );
    } finally {
      if (mounted) {
        setState(() {
          _isProcessing = false;
        });
      }
    }
  }

  Future<void> _handleClockOut() async {
    setState(() {
      _isProcessing = true;
    });

    Position? position;
    if (_useLocation) {
      position = await _getCurrentLocation();
      if (position == null && _useLocation) {
        setState(() {
          _isProcessing = false;
        });
        return;
      }
      setState(() {
        _currentPosition = position;
      });
    }

    try {
      await _apiService.clockOut(
        latitude: position?.latitude,
        longitude: position?.longitude,
        location: position != null
            ? '${position.latitude}, ${position.longitude}'
            : null,
      );

      if (!mounted) return;

      setState(() {
        _lastAction = 'Clock Out';
        _lastActionTime = DateTime.now();
      });

      // Refresh attendance records
      await _loadAttendanceRecords();

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Clocked out successfully!'),
          backgroundColor: Colors.orange,
        ),
      );
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            'Failed to clock out: ${e.toString().replaceAll('Exception: ', '')}',
          ),
          backgroundColor: Colors.red,
        ),
      );
    } finally {
      if (mounted) {
        setState(() {
          _isProcessing = false;
        });
      }
    }
  }
}
