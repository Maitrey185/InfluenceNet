# InfluenceNet Infrastructure Shutdown Script
# This script stops all infrastructure services

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  InfluenceNet Infrastructure Shutdown " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Stopping infrastructure services..." -ForegroundColor Yellow
docker-compose down

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✓ Infrastructure services stopped successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "To remove volumes (clean slate): docker-compose down -v" -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "✗ Failed to stop infrastructure services" -ForegroundColor Red
    exit 1
}
