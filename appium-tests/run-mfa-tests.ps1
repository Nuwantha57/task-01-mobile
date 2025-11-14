# Appium MFA Test Execution Script
# This script starts Appium server and runs MFA tests

Write-Host "====================================================" -ForegroundColor Cyan
Write-Host "  MFA Testing with Appium - Automated Execution" -ForegroundColor Cyan
Write-Host "====================================================" -ForegroundColor Cyan
Write-Host ""

# Check if Appium is installed
Write-Host "[1/6] Checking Appium installation..." -ForegroundColor Yellow
try {
    $appiumVersion = appium --version
    Write-Host "✓ Appium version: $appiumVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Appium is not installed!" -ForegroundColor Red
    Write-Host "Install it with: npm install -g appium" -ForegroundColor Yellow
    exit 1
}

# Check if Android emulator is running
Write-Host ""
Write-Host "[2/6] Checking Android emulator..." -ForegroundColor Yellow
$devices = adb devices
if ($devices -match "emulator-\d+\s+device") {
    Write-Host "✓ Android emulator is running" -ForegroundColor Green
} else {
    Write-Host "✗ No Android emulator found!" -ForegroundColor Red
    Write-Host "Please start an emulator first" -ForegroundColor Yellow
    exit 1
}

# Check if APK exists
Write-Host ""
Write-Host "[3/6] Checking APK file..." -ForegroundColor Yellow
$apkPath = "C:\Intern_Project_Code\task-01-mobile\build\app\outputs\flutter-apk\app-debug.apk"
if (Test-Path $apkPath) {
    Write-Host "✓ APK found: $apkPath" -ForegroundColor Green
} else {
    Write-Host "✗ APK not found!" -ForegroundColor Red
    Write-Host "Building APK..." -ForegroundColor Yellow
    flutter build apk --debug
    if ($LASTEXITCODE -ne 0) {
        Write-Host "✗ Failed to build APK!" -ForegroundColor Red
        exit 1
    }
    Write-Host "✓ APK built successfully" -ForegroundColor Green
}

# Start Appium server in background
Write-Host ""
Write-Host "[4/6] Starting Appium server..." -ForegroundColor Yellow
$appiumJob = Start-Job -ScriptBlock {
    appium --allow-insecure chromedriver_autodownload
}
Write-Host "✓ Appium server started (Job ID: $($appiumJob.Id))" -ForegroundColor Green
Write-Host "   Waiting for server to be ready..." -ForegroundColor Gray
Start-Sleep -Seconds 5

# Navigate to appium-tests directory
Write-Host ""
Write-Host "[5/6] Navigating to appium-tests directory..." -ForegroundColor Yellow
Set-Location -Path "C:\Intern_Project_Code\task-01-mobile\appium-tests"
Write-Host "✓ Changed directory to appium-tests" -ForegroundColor Green

# Run Maven tests
Write-Host ""
Write-Host "[6/6] Running MFA tests with Maven..." -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

try {
    mvn clean test -Dtest=MfaTest
    $testExitCode = $LASTEXITCODE
} finally {
    # Stop Appium server
    Write-Host ""
    Write-Host "Stopping Appium server..." -ForegroundColor Yellow
    Stop-Job -Job $appiumJob
    Remove-Job -Job $appiumJob
    Write-Host "✓ Appium server stopped" -ForegroundColor Green
}

# Return to project root
Set-Location -Path "C:\Intern_Project_Code\task-01-mobile"

# Display results
Write-Host ""
Write-Host "====================================================" -ForegroundColor Cyan
if ($testExitCode -eq 0) {
    Write-Host "  ✓ All MFA tests passed!" -ForegroundColor Green
} else {
    Write-Host "  ✗ Some tests failed. Check logs above." -ForegroundColor Red
}
Write-Host "====================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Test reports available at:" -ForegroundColor Yellow
Write-Host "  appium-tests/target/surefire-reports/" -ForegroundColor Gray
Write-Host ""

exit $testExitCode
