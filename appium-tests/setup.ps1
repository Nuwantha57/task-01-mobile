# Maven Project Setup Script
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Maven Project Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/3] Cleaning old build artifacts..." -ForegroundColor Yellow
mvn clean

Write-Host ""
Write-Host "[2/3] Downloading dependencies..." -ForegroundColor Yellow
mvn dependency:resolve

Write-Host ""
Write-Host "[3/3] Compiling project..." -ForegroundColor Yellow
mvn compile

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "If you still see errors in VS Code:" -ForegroundColor Yellow
Write-Host "1. Close VS Code" -ForegroundColor White
Write-Host "2. Reopen the appium-tests folder" -ForegroundColor White
Write-Host "3. Wait for Java Language Server to load" -ForegroundColor White
Write-Host ""
