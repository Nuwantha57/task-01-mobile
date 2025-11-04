@echo off
echo ========================================
echo Maven Project Setup
echo ========================================
echo.

echo [1/3] Cleaning old build artifacts...
call mvn clean

echo.
echo [2/3] Downloading dependencies...
call mvn dependency:resolve

echo.
echo [3/3] Compiling project...
call mvn compile

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo If you still see errors in VS Code:
echo 1. Close VS Code
echo 2. Reopen the appium-tests folder
echo 3. Wait for Java Language Server to load
echo.
pause
