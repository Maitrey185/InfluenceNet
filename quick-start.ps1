# Quick Start Script for InfluenceNet
# Fixes Windows classpath issues and starts the application

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "InfluenceNet Quick Start" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check Java version
Write-Host "Step 1: Checking Java version..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString() }
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found. Please install Java 17" -ForegroundColor Red
    exit 1
}

# Step 2: Stop any running Gradle daemons
Write-Host ""
Write-Host "Step 2: Stopping Gradle daemons..." -ForegroundColor Yellow
./gradlew --stop
Write-Host "✓ Gradle daemons stopped" -ForegroundColor Green

# Step 3: Start Docker services
Write-Host ""
Write-Host "Step 3: Starting Docker services..." -ForegroundColor Yellow
Write-Host "Starting PostgreSQL, MongoDB, and Redis..." -ForegroundColor Gray

docker-compose up -d postgres mongodb redis

Start-Sleep -Seconds 5

Write-Host "✓ Docker services started" -ForegroundColor Green

# Step 4: Wait for databases to be ready
Write-Host ""
Write-Host "Step 4: Waiting for databases to be ready..." -ForegroundColor Yellow
Write-Host "This may take 30-60 seconds..." -ForegroundColor Gray

$maxAttempts = 30
$attempt = 0
$ready = $false

while (-not $ready -and $attempt -lt $maxAttempts) {
    $attempt++
    Write-Host "Attempt $attempt/$maxAttempts..." -ForegroundColor Gray
    
    try {
        $pgReady = docker exec influencenet-postgres pg_isready -U influencenet_user -d influencenet 2>&1
        if ($pgReady -like "*accepting connections*") {
            $ready = $true
            Write-Host "✓ PostgreSQL is ready" -ForegroundColor Green
        }
    } catch {
        Start-Sleep -Seconds 2
    }
}

if (-not $ready) {
    Write-Host "⚠ Databases may not be fully ready. Continuing anyway..." -ForegroundColor Yellow
}

# Step 5: Clean and build
Write-Host ""
Write-Host "Step 5: Building application..." -ForegroundColor Yellow
./gradlew clean build -x test

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Build failed" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Build successful" -ForegroundColor Green

# Step 6: Run the application
Write-Host ""
Write-Host "Step 6: Starting application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Application Starting..." -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Access points:" -ForegroundColor White
Write-Host "  - Application: http://localhost:8080" -ForegroundColor Gray
Write-Host "  - Actuator Health: http://localhost:8080/actuator/health" -ForegroundColor Gray
Write-Host "  - Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Kafka UI: http://localhost:8090" -ForegroundColor Gray
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host ""

# Run with shortened classpath
$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8"
./gradlew bootRun
