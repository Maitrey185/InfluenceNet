# Authentication Service Setup Guide

## Overview

This guide covers the complete authentication service implementation using **Keycloak 23** and **Spring Security OAuth2**.

---

## Architecture

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Frontend  │─────▶│  Spring Boot │─────▶│  Keycloak   │
│   (React)   │◀─────│   Auth API   │◀─────│   Server    │
└─────────────┘      └──────────────┘      └─────────────┘
                            │
                            ▼
                     ┌──────────────┐
                     │  PostgreSQL  │
                     │   (Users)    │
                     └──────────────┘
```

---

## Features Implemented

✅ **User Registration** with role selection (INFLUENCER/BRAND)  
✅ **User Login** with JWT token generation  
✅ **Token Refresh** for seamless authentication  
✅ **Get Current User** information from JWT  
✅ **Forgot Password** flow with email  
✅ **Password Reset** via Keycloak email  
✅ **Role-Based Access Control** (RBAC)  
✅ **Email Verification** (optional)  
✅ **Swagger/OpenAPI** documentation  

---

## Project Structure

```
src/main/java/com/project/InfluenceNet/
├── auth/
│   ├── controller/
│   │   └── AuthController.java          # REST endpoints
│   ├── dto/
│   │   ├── RegisterRequest.java         # Registration payload
│   │   ├── LoginRequest.java            # Login payload
│   │   ├── AuthResponse.java            # Token response
│   │   ├── RefreshTokenRequest.java     # Refresh token payload
│   │   ├── ForgotPasswordRequest.java   # Forgot password payload
│   │   └── ResetPasswordRequest.java    # Reset password payload
│   └── service/
│       └── AuthService.java             # Business logic
├── config/
│   ├── KeycloakConfig.java              # Keycloak admin client
│   ├── SecurityConfig.java              # Spring Security config
│   ├── OpenApiConfig.java               # Swagger configuration
│   └── RestTemplateConfig.java          # HTTP client
└── exception/
    ├── GlobalExceptionHandler.java      # Error handling
    └── ErrorResponse.java               # Error DTO

config/keycloak/
└── realm-export.json                    # Keycloak realm configuration
```

---

## Setup Instructions

### 1. Start Infrastructure

```bash
# Start all services including Keycloak
docker-compose up -d

# Check if Keycloak is running
docker-compose ps keycloak
```

**Keycloak will be available at**: http://localhost:8180

### 2. Verify Keycloak Realm Import

Access Keycloak Admin Console:
- URL: http://localhost:8180
- Username: `admin`
- Password: `admin`

Check if `influencenet` realm exists:
1. Click on realm dropdown (top-left)
2. You should see "influencenet" realm
3. If not, manually import `config/keycloak/realm-export.json`

### 3. Verify Realm Configuration

In the `influencenet` realm, verify:

**Roles** (Realm Roles):
- ✅ INFLUENCER
- ✅ BRAND
- ✅ ADMIN

**Clients**:
- ✅ `influencenet-backend` (confidential client)
- ✅ `influencenet-frontend` (public client)

**SMTP Settings** (Realm Settings → Email):
- Host: `localhost`
- Port: `1025`
- From: `noreply@influencenet.local`

### 4. Build and Run Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

**Application will start on**: http://localhost:8080

### 5. Access Swagger UI

Open your browser:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

---

## API Endpoints

### Base URL
```
http://localhost:8080/api/auth
```

### 1. Register User

**POST** `/api/auth/register`

**Request Body**:
```json
{
  "email": "john.doe@example.com",
  "username": "johndoe",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "role": "INFLUENCER"
}
```

**Response** (201 Created):
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "user": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "INFLUENCER",
    "emailVerified": false
  }
}
```

**Validation Rules**:
- Email: Valid email format
- Username: 3-50 characters, alphanumeric + underscore/hyphen
- Password: Min 8 chars, must contain uppercase, lowercase, number, special char
- Role: Must be `INFLUENCER` or `BRAND`

---

### 2. Login

**POST** `/api/auth/login`

**Request Body**:
```json
{
  "usernameOrEmail": "johndoe",
  "password": "SecurePass123!"
}
```

**Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "user": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "INFLUENCER",
    "emailVerified": false
  }
}
```

**Error Response** (401 Unauthorized):
```json
{
  "timestamp": "2025-10-31T00:00:00",
  "status": 401,
  "error": "Authentication Failed",
  "message": "Invalid username or password"
}
```

---

### 3. Refresh Token

**POST** `/api/auth/refresh`

**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

---

### 4. Get Current User

**GET** `/api/auth/me`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response** (200 OK):
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "INFLUENCER",
  "emailVerified": false
}
```

---

### 5. Forgot Password

**POST** `/api/auth/forgot-password`

**Request Body**:
```json
{
  "email": "john.doe@example.com"
}
```

**Response** (200 OK):
```json
{
  "message": "If the email exists, a password reset link has been sent"
}
```

**Note**: Check Mailhog at http://localhost:8025 for the reset email.

---

### 6. Reset Password

**POST** `/api/auth/reset-password`

**Request Body**:
```json
{
  "token": "reset-token-from-email",
  "newPassword": "NewSecurePass123!"
}
```

**Response** (501 Not Implemented):
```json
{
  "message": "Password reset is handled through Keycloak email flow. Use the link sent to your email to reset your password."
}
```

**Note**: Password reset is handled by clicking the link in the email, which redirects to Keycloak's password reset page.

---

## Testing with cURL

### Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "influencer@test.com",
    "username": "testinfluencer",
    "password": "Test1234!",
    "firstName": "Test",
    "lastName": "Influencer",
    "role": "INFLUENCER"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testinfluencer",
    "password": "Test1234!"
  }'
```

### Get Current User (with token)

```bash
# Replace YOUR_ACCESS_TOKEN with actual token from login response
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Refresh Token

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

---

## Testing with Postman

### 1. Import Collection

Create a new Postman collection with the following requests:

**Collection Variables**:
- `baseUrl`: `http://localhost:8080`
- `accessToken`: (will be set automatically)
- `refreshToken`: (will be set automatically)

### 2. Register Request

```
POST {{baseUrl}}/api/auth/register
Content-Type: application/json

Body (raw JSON):
{
  "email": "brand@test.com",
  "username": "testbrand",
  "password": "Test1234!",
  "firstName": "Test",
  "lastName": "Brand",
  "role": "BRAND"
}

Tests (to save tokens):
pm.test("Status is 201", function() {
    pm.response.to.have.status(201);
});

var jsonData = pm.response.json();
pm.collectionVariables.set("accessToken", jsonData.accessToken);
pm.collectionVariables.set("refreshToken", jsonData.refreshToken);
```

### 3. Login Request

```
POST {{baseUrl}}/api/auth/login
Content-Type: application/json

Body:
{
  "usernameOrEmail": "testbrand",
  "password": "Test1234!"
}

Tests:
var jsonData = pm.response.json();
pm.collectionVariables.set("accessToken", jsonData.accessToken);
pm.collectionVariables.set("refreshToken", jsonData.refreshToken);
```

### 4. Get Current User

```
GET {{baseUrl}}/api/auth/me
Authorization: Bearer {{accessToken}}
```

---

## JWT Token Structure

### Access Token Claims

```json
{
  "exp": 1698765432,
  "iat": 1698764532,
  "jti": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "iss": "http://localhost:8180/realms/influencenet",
  "sub": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "typ": "Bearer",
  "azp": "influencenet-backend",
  "realm_access": {
    "roles": ["INFLUENCER"]
  },
  "email": "john.doe@example.com",
  "preferred_username": "johndoe",
  "given_name": "John",
  "family_name": "Doe"
}
```

### Decode JWT

Use https://jwt.io to decode and inspect tokens.

---

## Security Features

### 1. Password Requirements

- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one number
- At least one special character (@$!%*?&)

### 2. Token Expiration

- **Access Token**: 15 minutes (900 seconds)
- **Refresh Token**: 30 minutes (configurable in Keycloak)

### 3. Brute Force Protection

Keycloak has built-in brute force protection:
- Max 5 failed login attempts
- Account locked for 15 minutes after max failures

### 4. CORS Configuration

Allowed origins:
- `http://localhost:3000` (React dev server)
- `http://localhost:5173` (Vite dev server)

---

## Email Testing with Mailhog

### Access Mailhog

**URL**: http://localhost:8025

### Test Email Flows

1. **Email Verification**:
   - Register a new user
   - Check Mailhog for verification email
   - Click verification link

2. **Password Reset**:
   - Request password reset
   - Check Mailhog for reset email
   - Click reset link
   - Set new password

---

## Role-Based Access Control

### Protecting Endpoints

Use `@PreAuthorize` annotation in controllers:

```java
@GetMapping("/influencer/dashboard")
@PreAuthorize("hasRole('INFLUENCER')")
public ResponseEntity<?> getInfluencerDashboard() {
    // Only accessible by INFLUENCER role
}

@GetMapping("/brand/campaigns")
@PreAuthorize("hasRole('BRAND')")
public ResponseEntity<?> getBrandCampaigns() {
    // Only accessible by BRAND role
}

@GetMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> getAllUsers() {
    // Only accessible by ADMIN role
}
```

### Extracting User Info in Controllers

```java
@GetMapping("/profile")
public ResponseEntity<?> getProfile(Authentication authentication) {
    Jwt jwt = (Jwt) authentication.getPrincipal();
    String userId = jwt.getSubject();
    String username = jwt.getClaim("preferred_username");
    String email = jwt.getClaim("email");
    
    // Use user info...
}
```

---

## Troubleshooting

### Issue: Keycloak realm not imported

**Solution**:
1. Access Keycloak admin console: http://localhost:8180
2. Login with admin/admin
3. Click realm dropdown → "Create Realm"
4. Import `config/keycloak/realm-export.json`

### Issue: "Invalid credentials" on login

**Possible causes**:
1. User doesn't exist - register first
2. Wrong password
3. Keycloak not running - check `docker-compose ps`

### Issue: "401 Unauthorized" on protected endpoints

**Possible causes**:
1. Missing Authorization header
2. Expired access token - use refresh token
3. Invalid token format - should be `Bearer <token>`

### Issue: Email not sent

**Check**:
1. Mailhog is running: `docker-compose ps mailhog`
2. Mailhog UI: http://localhost:8025
3. Keycloak SMTP settings in realm configuration

### Issue: CORS errors from frontend

**Solution**:
Add frontend URL to `SecurityConfig.java`:
```java
configuration.setAllowedOrigins(List.of(
    "http://localhost:3000",
    "http://localhost:5173",
    "http://your-frontend-url"
));
```

---

## Next Steps

### 1. Add User Profile Management

Create endpoints to update user profile:
- Update profile (name, bio, avatar)
- Change password
- Update email (with verification)

### 2. Add Social Login

Integrate OAuth2 providers:
- Google
- Facebook
- Instagram

### 3. Add Two-Factor Authentication (2FA)

Enable OTP-based 2FA in Keycloak.

### 4. Add Audit Logging

Log all authentication events:
- Login attempts
- Password changes
- Role changes

### 5. Add Rate Limiting

Implement rate limiting for auth endpoints to prevent abuse.

---

## Configuration Reference

### application.properties

```properties
# Keycloak OAuth2 Resource Server
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8180/realms/influencenet
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8180/realms/influencenet/protocol/openid-connect/certs

# Keycloak Admin Client
app.keycloak.server-url=http://localhost:8180
app.keycloak.realm=influencenet
app.keycloak.admin-username=admin
app.keycloak.admin-password=admin
app.keycloak.client-id=influencenet-backend
app.keycloak.client-secret=influencenet-backend-secret
```

### docker-compose.yml (Keycloak)

```yaml
keycloak:
  image: quay.io/keycloak/keycloak:23.0
  command: start-dev --import-realm
  environment:
    KEYCLOAK_ADMIN: admin
    KEYCLOAK_ADMIN_PASSWORD: admin
    KC_DB: postgres
    KC_DB_URL: jdbc:postgresql://postgres:5432/keycloak
  ports:
    - "8180:8080"
  volumes:
    - ./config/keycloak:/opt/keycloak/data/import
```

---

## Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [JWT.io](https://jwt.io) - JWT decoder
- [Mailhog](https://github.com/mailhog/MailHog) - Email testing

---

**Last Updated**: 2025-10-31  
**Version**: 1.0.0
