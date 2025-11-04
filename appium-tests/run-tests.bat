@echo off
REM Appium Mobile Test Runner
REM This script runs the mobile automation tests

echo ========================================
echo Mobile Automation Test Runner
echo ========================================
echo.

REM Check if Appium is running
echo [1/3] Checking prerequisites...
curl -s http://127.0.0.1:4723/status >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Appium server is not running on port 4723
    echo Please start Appium in another terminal: appium
    pause
    exit /b 1
)
echo [OK] Appium server is running

REM Check if emulator is running
echo [2/3] Checking Android emulator...
adb devices | findstr "emulator" >nul
if errorlevel 1 (
    echo [WARNING] No emulator detected
    echo Starting emulator...
    start cmd /k "emulator -avd \"Medium Phone API 36.1\""
    echo Waiting for emulator to start (30 seconds)...
    timeout /t 30 /nobreak
)
echo [OK] Android device/emulator ready

REM Run tests
echo [3/3] Running tests...
echo.
mvn clean test

echo.
echo ========================================
echo Test execution complete!
echo Check reports in target/surefire-reports/
echo ========================================
pause
