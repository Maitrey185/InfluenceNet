# Instagram Webhook Setup Script for Windows
# This script helps you set up and test your Instagram webhook

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Instagram Webhook Setup Assistant" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Generate verify token
Write-Host "Step 1: Generate Verify Token" -ForegroundColor Yellow
$verifyToken = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 32 | ForEach-Object {[char]$_})
Write-Host "Generated Verify Token: $verifyToken" -ForegroundColor Green
Write-Host ""

# Step 2: Set environment variables
Write-Host "Step 2: Setting Environment Variables" -ForegroundColor Yellow
$env:INSTAGRAM_WEBHOOK_VERIFY_TOKEN = $verifyToken
Write-Host "✓ INSTAGRAM_WEBHOOK_VERIFY_TOKEN set" -ForegroundColor Green

# Ask for App Secret
Write-Host ""
Write-Host "Enter your Instagram App Secret (from Facebook Developer Dashboard):" -ForegroundColor Yellow
Write-Host "(Leave empty to skip for now)" -ForegroundColor Gray
$appSecret = Read-Host
if ($appSecret) {
    $env:INSTAGRAM_APP_SECRET = $appSecret
    Write-Host "✓ INSTAGRAM_APP_SECRET set" -ForegroundColor Green
}
Write-Host ""

# Step 3: Check if ngrok is installed
Write-Host "Step 3: Checking for ngrok" -ForegroundColor Yellow
$ngrokInstalled = Get-Command ngrok -ErrorAction SilentlyContinue
if ($ngrokInstalled) {
    Write-Host "✓ ngrok is installed" -ForegroundColor Green
    
    Write-Host ""
    Write-Host "Starting ngrok tunnel on port 8080..." -ForegroundColor Yellow
    Write-Host "Opening ngrok in a new window..." -ForegroundColor Gray
    
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "ngrok http 8080"
    Start-Sleep -Seconds 3
    
    # Try to get ngrok URL
    try {
        $ngrokApi = Invoke-RestMethod -Uri "http://localhost:4040/api/tunnels" -ErrorAction SilentlyContinue
        $publicUrl = $ngrokApi.tunnels[0].public_url
        Write-Host ""
        Write-Host "✓ ngrok tunnel started!" -ForegroundColor Green
        Write-Host "Public URL: $publicUrl" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Your webhook URL is:" -ForegroundColor Yellow
        Write-Host "$publicUrl/webhook/instagram" -ForegroundColor Green
    } catch {
        Write-Host "⚠ Could not retrieve ngrok URL automatically" -ForegroundColor Yellow
        Write-Host "Check the ngrok window for your public URL" -ForegroundColor Gray
    }
} else {
    Write-Host "✗ ngrok is not installed" -ForegroundColor Red
    Write-Host ""
    Write-Host "To install ngrok:" -ForegroundColor Yellow
    Write-Host "1. Download from: https://ngrok.com/download" -ForegroundColor Gray
    Write-Host "2. Extract to a folder in your PATH" -ForegroundColor Gray
    Write-Host "3. Run: ngrok config add-authtoken YOUR_TOKEN" -ForegroundColor Gray
    Write-Host "4. Run this script again" -ForegroundColor Gray
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Configuration Summary" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Verify Token: $verifyToken" -ForegroundColor White
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. Start your Spring Boot application" -ForegroundColor White
Write-Host "2. Go to Facebook Developer Dashboard" -ForegroundColor White
Write-Host "3. Navigate to: Your App → Instagram → Configuration" -ForegroundColor White
Write-Host "4. Add webhook with:" -ForegroundColor White
Write-Host "   - Callback URL: https://YOUR-NGROK-URL.ngrok.io/webhook/instagram" -ForegroundColor Gray
Write-Host "   - Verify Token: $verifyToken" -ForegroundColor Gray
Write-Host "5. Click 'Verify and Save'" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
