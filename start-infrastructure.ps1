# InfluenceNet Infrastructure Startup Script
# This script starts all required infrastructure services using Docker Compose

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  InfluenceNet Infrastructure Startup  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Docker is running
Write-Host "Checking Docker status..." -ForegroundColor Yellow
try {
    docker info | Out-Null
    Write-Host "✓ Docker is running" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker is not running. Please start Docker Desktop." -ForegroundColor Red
    exit 1
}

# Check if .env file exists
if (-Not (Test-Path ".env")) {
    Write-Host "⚠ .env file not found. Creating from .env.example..." -ForegroundColor Yellow
    Copy-Item ".env.example" ".env"
    Write-Host "✓ .env file created. Please review and update if needed." -ForegroundColor Green
}

# Start Docker Compose
Write-Host ""
Write-Host "Starting infrastructure services..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✓ Infrastructure services started successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Waiting for services to be healthy..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10
    
    Write-Host ""
    Write-Host "Service Status:" -ForegroundColor Cyan
    docker-compose ps
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  Service URLs                         " -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "Kafka UI:        http://localhost:8080" -ForegroundColor White
    Write-Host "Kibana:          http://localhost:5601" -ForegroundColor White
    Write-Host "Neo4j Browser:   http://localhost:7474" -ForegroundColor White
    Write-Host "MinIO Console:   http://localhost:9001" -ForegroundColor White
    Write-Host "Keycloak:        http://localhost:8180" -ForegroundColor White
    Write-Host "Zipkin:          http://localhost:9411" -ForegroundColor White
    Write-Host "Prometheus:      http://localhost:9090" -ForegroundColor White
    Write-Host "Grafana:         http://localhost:3000" -ForegroundColor White
    Write-Host "Mailhog:         http://localhost:8025" -ForegroundColor White
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "To view logs: docker-compose logs -f" -ForegroundColor Yellow
    Write-Host "To stop: docker-compose down" -ForegroundColor Yellow
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "✗ Failed to start infrastructure services" -ForegroundColor Red
    Write-Host "Check logs with: docker-compose logs" -ForegroundColor Yellow
    exit 1
}
