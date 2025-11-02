# Authentication Service - Quick Start

## 🚀 5-Minute Setup

### 1. Start Services (1 min)
```bash
docker-compose up -d
```

### 2. Run Application (1 min)
```bash
./gradlew bootRun
```

### 3. Test Registration (1 min)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@test.com",
    "username": "testuser",
    "password": "Test1234!",
    "firstName": "Test",
    "lastName": "User",
    "role": "INFLUENCER"
  }'
```

### 4. Test Login (1 min)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "Test1234!"
  }'
```

### 5. Access Swagger (1 min)
Open: http://localhost:8080/swagger-ui.html

---

## 📋 Quick Reference

### Endpoints
| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/auth/register` | POST | Register user |
| `/api/auth/login` | POST | Login |
| `/api/auth/me` | GET | Get user info |
| `/api/auth/refresh` | POST | Refresh token |

### URLs
- **API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **Keycloak**: http://localhost:8180 (admin/admin)
- **Mailhog**: http://localhost:8025

### Test Users
Create with `/api/auth/register`:
- **Influencer**: role = "INFLUENCER"
- **Brand**: role = "BRAND"

### Password Rules
- Min 8 characters
- 1 uppercase, 1 lowercase
- 1 number, 1 special char
- Example: `Test1234!`

---

## 🧪 Quick Test

### PowerShell
```powershell
.\test-auth.ps1
```

### Postman
Import: `postman/InfluenceNet-Auth-API.postman_collection.json`

---

## 📖 Full Documentation
See [AUTH_SERVICE_GUIDE.md](AUTH_SERVICE_GUIDE.md)
