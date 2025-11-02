# InfluenceNet Authentication Service Test Script
# This script tests all authentication endpoints

$baseUrl = "http://localhost:8080/api/auth"
$headers = @{
    "Content-Type" = "application/json"
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "InfluenceNet Auth Service Test" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Register Influencer
Write-Host "1. Testing User Registration (Influencer)..." -ForegroundColor Yellow
$registerBody = @{
    email = "influencer@test.com"
    username = "testinfluencer"
    password = "Test1234!"
    firstName = "Test"
    lastName = "Influencer"
    role = "INFLUENCER"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/register" -Method Post -Headers $headers -Body $registerBody
    Write-Host "✓ Registration successful!" -ForegroundColor Green
    Write-Host "  User ID: $($registerResponse.user.id)" -ForegroundColor Gray
    Write-Host "  Username: $($registerResponse.user.username)" -ForegroundColor Gray
    Write-Host "  Role: $($registerResponse.user.role)" -ForegroundColor Gray
    $accessToken = $registerResponse.accessToken
    $refreshToken = $registerResponse.refreshToken
} catch {
    Write-Host "✗ Registration failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: Login
Write-Host "2. Testing User Login..." -ForegroundColor Yellow
$loginBody = @{
    usernameOrEmail = "testinfluencer"
    password = "Test1234!"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/login" -Method Post -Headers $headers -Body $loginBody
    Write-Host "✓ Login successful!" -ForegroundColor Green
    Write-Host "  Access Token: $($loginResponse.accessToken.Substring(0, 50))..." -ForegroundColor Gray
    Write-Host "  Expires In: $($loginResponse.expiresIn) seconds" -ForegroundColor Gray
    $accessToken = $loginResponse.accessToken
    $refreshToken = $loginResponse.refreshToken
} catch {
    Write-Host "✗ Login failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: Get Current User
Write-Host "3. Testing Get Current User..." -ForegroundColor Yellow
$authHeaders = @{
    "Authorization" = "Bearer $accessToken"
}

try {
    $userResponse = Invoke-RestMethod -Uri "$baseUrl/me" -Method Get -Headers $authHeaders
    Write-Host "✓ User info retrieved!" -ForegroundColor Green
    Write-Host "  ID: $($userResponse.id)" -ForegroundColor Gray
    Write-Host "  Username: $($userResponse.username)" -ForegroundColor Gray
    Write-Host "  Email: $($userResponse.email)" -ForegroundColor Gray
    Write-Host "  Role: $($userResponse.role)" -ForegroundColor Gray
    Write-Host "  Email Verified: $($userResponse.emailVerified)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Get user failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 4: Refresh Token
Write-Host "4. Testing Token Refresh..." -ForegroundColor Yellow
$refreshBody = @{
    refreshToken = $refreshToken
} | ConvertTo-Json

try {
    $refreshResponse = Invoke-RestMethod -Uri "$baseUrl/refresh" -Method Post -Headers $headers -Body $refreshBody
    Write-Host "✓ Token refreshed!" -ForegroundColor Green
    Write-Host "  New Access Token: $($refreshResponse.accessToken.Substring(0, 50))..." -ForegroundColor Gray
} catch {
    Write-Host "✗ Token refresh failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 5: Forgot Password
Write-Host "5. Testing Forgot Password..." -ForegroundColor Yellow
$forgotBody = @{
    email = "influencer@test.com"
} | ConvertTo-Json

try {
    $forgotResponse = Invoke-RestMethod -Uri "$baseUrl/forgot-password" -Method Post -Headers $headers -Body $forgotBody
    Write-Host "✓ Password reset email sent!" -ForegroundColor Green
    Write-Host "  Message: $($forgotResponse.message)" -ForegroundColor Gray
    Write-Host "  Check Mailhog at http://localhost:8025" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Forgot password failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 6: Register Brand
Write-Host "6. Testing User Registration (Brand)..." -ForegroundColor Yellow
$brandBody = @{
    email = "brand@test.com"
    username = "testbrand"
    password = "Test1234!"
    firstName = "Test"
    lastName = "Brand"
    role = "BRAND"
} | ConvertTo-Json

try {
    $brandResponse = Invoke-RestMethod -Uri "$baseUrl/register" -Method Post -Headers $headers -Body $brandBody
    Write-Host "✓ Brand registration successful!" -ForegroundColor Green
    Write-Host "  User ID: $($brandResponse.user.id)" -ForegroundColor Gray
    Write-Host "  Role: $($brandResponse.user.role)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Brand registration failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Test Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "All authentication endpoints tested!" -ForegroundColor Green
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. Check Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "2. Check Mailhog: http://localhost:8025" -ForegroundColor White
Write-Host "3. Check Keycloak: http://localhost:8180" -ForegroundColor White
Write-Host ""
