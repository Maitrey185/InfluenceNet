# Authentication Configuration Guide

## Current Status: **DISABLED** ✅

Authentication has been temporarily disabled to allow development without Keycloak setup.

## What Was Disabled

### 1. Security Configuration
- **File**: `src/main/java/com/project/InfluenceNet/config/SecurityConfig.java`
- **Status**: All code commented out (Spring Security filter chain, JWT decoder, CORS config)
- **To Re-enable**: Uncomment all code and annotations

### 2. Keycloak Configuration
- **File**: `src/main/java/com/project/InfluenceNet/config/KeycloakConfig.java`
- **Status**: `@Configuration` and `@Value` annotations commented out
- **To Re-enable**: Uncomment annotations and imports

### 3. Auth Service
- **File**: `src/main/java/com/project/InfluenceNet/auth/service/AuthService.java`
- **Status**: All Keycloak integration code commented out
- **To Re-enable**: Uncomment all code

### 4. Auth Controller
- **File**: `src/main/java/com/project/InfluenceNet/auth/controller/AuthController.java`
- **Status**: `@RestController` and all endpoints commented out
- **To Re-enable**: Uncomment all code and annotations

### 5. Dependencies
- **File**: `build.gradle.kts`
- **Commented Dependencies**:
  - `spring-boot-starter-security`
  - `spring-boot-starter-oauth2-resource-server`
  - `spring-boot-starter-oauth2-client`
  - `keycloak-admin-client`
- **To Re-enable**: Uncomment these dependencies

### 6. Application Properties
- **File**: `src/main/resources/application.properties`
- **Commented Properties**:
  - Spring Security OAuth2 configuration (lines 157-168)
  - Keycloak admin configuration (lines 175-180)
- **To Re-enable**: Uncomment these properties

## How to Re-enable Authentication

### Step 1: Start Keycloak Server
```bash
# Make sure Keycloak is running on http://localhost:8180
docker run -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

### Step 2: Configure Keycloak
1. Create realm: `influencenet`
2. Create client: `influencenet-backend`
3. Set client secret: `influencenet-backend-secret`
4. Create roles: `INFLUENCER`, `BRAND`, `ADMIN`
5. Configure client settings for OAuth2

### Step 3: Uncomment Dependencies
In `build.gradle.kts`, uncomment:
```kotlin
implementation("org.springframework.boot:spring-boot-starter-security")
implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
implementation("org.keycloak:keycloak-admin-client:23.0.0")
```

### Step 4: Uncomment Application Properties
In `application.properties`, uncomment:
- Lines 157-168 (Spring Security OAuth2)
- Lines 175-180 (Keycloak admin config)

### Step 5: Uncomment Configuration Classes
1. **SecurityConfig.java**: Uncomment `@Configuration`, `@EnableWebSecurity`, `@EnableMethodSecurity` and all code
2. **KeycloakConfig.java**: Uncomment `@Configuration`, `@Value` annotations and all imports
3. **AuthService.java**: Uncomment all Keycloak integration code
4. **AuthController.java**: Uncomment `@RestController`, `@RequestMapping` and all endpoints

### Step 6: Rebuild and Run
```bash
./gradlew clean build
./gradlew bootRun
```

## Testing Without Authentication

While authentication is disabled, you can test the API using:

### Health Check Endpoint
```bash
curl http://localhost:8080/api/health
```

### Status Endpoint
```bash
curl http://localhost:8080/api/status
```

## Security Considerations

⚠️ **WARNING**: With authentication disabled, all endpoints are publicly accessible!

- Do NOT deploy to production with authentication disabled
- Do NOT expose this configuration to the internet
- Use only for local development
- Re-enable authentication before deploying to any shared environment

## Protected Endpoints (When Auth is Enabled)

The following endpoints will require authentication when re-enabled:
- All endpoints except:
  - `/api/auth/register`
  - `/api/auth/login`
  - `/api/auth/refresh`
  - `/api/auth/forgot-password`
  - `/api/auth/reset-password`
  - `/actuator/**`
  - `/swagger-ui/**`
  - `/v3/api-docs/**`

## Quick Re-enable Checklist

- [ ] Keycloak server running
- [ ] Realm and client configured
- [ ] Dependencies uncommented in `build.gradle.kts`
- [ ] Properties uncommented in `application.properties`
- [ ] `SecurityConfig.java` uncommented
- [ ] `KeycloakConfig.java` uncommented
- [ ] `AuthService.java` uncommented
- [ ] `AuthController.java` uncommented
- [ ] Application rebuilt
- [ ] Test login endpoint

## Support

For issues or questions about authentication setup, refer to:
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Boot OAuth2 Guide](https://spring.io/guides/tutorials/spring-boot-oauth2/)
