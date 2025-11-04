# PowerShell Test Runner
# Run specific tests easily

param(
    [string]$TestClass = "LoginTest",
    [string]$TestMethod = ""
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Mobile Automation Test Runner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Appium
Write-Host "[1/3] Checking Appium server..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://127.0.0.1:4723/status" -UseBasicParsing -ErrorAction Stop
    Write-Host "[OK] Appium is running" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Appium not running on port 4723" -ForegroundColor Red
    Write-Host "Please start Appium: appium" -ForegroundColor Yellow
    exit 1
}

# Check device
Write-Host "[2/3] Checking Android device..." -ForegroundColor Yellow
$devices = adb devices
if ($devices -match "emulator") {
    Write-Host "[OK] Emulator detected" -ForegroundColor Green
} else {
    Write-Host "[WARNING] No emulator found" -ForegroundColor Yellow
}

# Run tests
Write-Host "[3/3] Running tests..." -ForegroundColor Yellow
Write-Host ""

if ($TestMethod -eq "") {
    Write-Host "Running all tests in $TestClass..." -ForegroundColor Cyan
    mvn test -Dtest=$TestClass
} else {
    Write-Host "Running $TestClass#$TestMethod..." -ForegroundColor Cyan
    mvn test -Dtest="$TestClass#$TestMethod"
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Test execution complete!" -ForegroundColor Green
Write-Host "Reports: target/surefire-reports/" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
