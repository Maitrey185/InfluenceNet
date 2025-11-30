# InfluenceNet Project Structure Guide

## **Quick Reference**

### **Current Status** ✅
- ✅ Spring Boot 3.5.7 configured
- ✅ Docker Compose with all infrastructure
- ✅ Postgres, MongoDB, Redis, Kafka, Elasticsearch, Neo4j
- ✅ Flyway migrations
- ✅ Basic application.properties configured

### **Next Steps** 🚀
1. Create modular structure
2. Implement User Service
3. Implement Influencer Service
4. Add Instagram connector

---

## **Recommended Module Implementation Order**

### **Phase 1: Foundation (Weeks 1-6)**
```
1. common-domain          → Shared domain models
2. common-infrastructure  → Shared configs
3. common-application     → Shared DTOs
4. user-service          → Authentication
5. notification-service  → Email notifications
```

### **Phase 2: Core Features (Weeks 7-17)**
```
6. influencer-service       → Profile management
7. social-connector-service → Instagram integration
8. enrichment-service       → Data processing
9. analytics-service        → KPIs & metrics
10. gateway                 → API Gateway
```

### **Phase 3: Brand & Campaigns (Weeks 18-28)**
```
11. brand-service    → Brand profiles
12. campaign-service → Campaign management
13. payment-service  → Stripe integration
```

### **Phase 4: Advanced Features (Weeks 29-40)**
```
14. search-service          → Elasticsearch
15. graph-service           → Neo4j
16. recommendation-service  → AI recommendations
```

---

## **Module Template**

When creating a new module, follow this structure:

```
module-name-service/
├── module-domain/
│   └── src/main/java/com/project/influencenet/modulename/domain/
│       ├── entity/          # JPA entities
│       ├── repository/      # Repository interfaces
│       ├── service/         # Domain services
│       └── event/           # Domain events
│
├── module-application/
│   └── src/main/java/com/project/influencenet/modulename/application/
│       ├── dto/             # Request/Response DTOs
│       ├── mapper/          # Entity ↔ DTO mappers
│       ├── service/         # Application services (use cases)
│       └── port/            # Input/Output ports
│
├── module-infrastructure/
│   └── src/main/java/com/project/influencenet/modulename/infrastructure/
│       ├── adapter/
│       │   ├── persistence/ # JPA repository implementations
│       │   ├── messaging/   # Kafka producers/consumers
│       │   └── external/    # External API clients
│       └── config/          # Module-specific configuration
│
└── module-api/
    └── src/main/java/com/project/influencenet/modulename/api/
        ├── controller/      # REST controllers
        ├── filter/          # Request filters
        └── exception/       # Exception handlers
```

---

## **File Naming Conventions**

### **Entities**
```java
// Domain entity
InfluencerProfile.java
SocialAccount.java
Campaign.java
```

### **Repositories**
```java
// Interface in domain layer
InfluencerProfileRepository.java

// Implementation in infrastructure layer
InfluencerProfileRepositoryImpl.java
```

### **Services**
```java
// Domain service
InfluencerDomainService.java

// Application service
ProfileManagementService.java
SocialAccountService.java
```

### **DTOs**
```java
// Request DTOs
CreateInfluencerProfileRequest.java
UpdateProfileRequest.java

// Response DTOs
InfluencerProfileResponse.java
SocialAccountResponse.java
```

### **Controllers**
```java
InfluencerProfileController.java
SocialAccountController.java
```

### **Events**
```java
// Domain events
ProfileCreatedEvent.java
SocialAccountLinkedEvent.java

// Event publishers
ProfileEventPublisher.java

// Event consumers
ProfileCreatedConsumer.java
```

---

## **Package Structure Example**

```
com.project.influencenet.influencer.domain.entity
com.project.influencenet.influencer.domain.repository
com.project.influencenet.influencer.domain.service
com.project.influencenet.influencer.domain.event

com.project.influencenet.influencer.application.dto
com.project.influencenet.influencer.application.service
com.project.influencenet.influencer.application.mapper

com.project.influencenet.influencer.infrastructure.adapter.persistence
com.project.influencenet.influencer.infrastructure.adapter.messaging
com.project.influencenet.influencer.infrastructure.config

com.project.influencenet.influencer.api.controller
com.project.influencenet.influencer.api.exception
```

---

## **Database Schema Organization**

### **Flyway Migrations**
```
src/main/resources/db/migration/
├── V1__initial_schema.sql           # Users, base tables
├── V2__influencer_profiles.sql      # Influencer module
├── V3__social_accounts.sql          # Social connector module
├── V4__campaigns.sql                # Campaign module
├── V5__payments.sql                 # Payment module
└── V6__analytics.sql                # Analytics module
```

### **Schema Naming**
- Use lowercase with underscores: `influencer_profiles`
- Foreign keys: `influencer_id`, `campaign_id`
- Timestamps: `created_at`, `updated_at`
- Boolean flags: `is_active`, `is_verified`

---

## **API Endpoint Conventions**

### **RESTful Patterns**
```
GET    /api/influencers              # List all
GET    /api/influencers/{id}         # Get one
POST   /api/influencers              # Create
PUT    /api/influencers/{id}         # Update
DELETE /api/influencers/{id}         # Delete

# Nested resources
GET    /api/influencers/{id}/social-accounts
POST   /api/influencers/{id}/social-accounts
DELETE /api/influencers/{id}/social-accounts/{platform}

# Actions
POST   /api/campaigns/{id}/invite
POST   /api/campaigns/{id}/accept
POST   /api/campaigns/{id}/complete
```

### **Query Parameters**
```
GET /api/influencers?page=0&size=20&sort=createdAt,desc
GET /api/analytics/kpis?start_date=2025-01-01&end_date=2025-01-31&platform=instagram
GET /api/search/influencers?q=fitness&niche=health&min_followers=10000
```

---

## **Configuration Files**

### **application.properties**
```properties
# Module-specific properties
influencer.profile.max-bio-length=1000
social.connector.instagram.polling-interval=6h
analytics.kpi.calculation-schedule=0 0 2 * * *
```

### **application-dev.yml**
```yaml
spring:
  profiles: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/influencenet
```

### **application-prod.yml**
```yaml
spring:
  profiles: prod
  datasource:
    url: ${DATABASE_URL}
```

---

## **Testing Structure**

```
src/test/java/com/project/influencenet/modulename/
├── domain/
│   ├── entity/              # Entity tests
│   └── service/             # Domain service tests
├── application/
│   └── service/             # Application service tests
├── infrastructure/
│   └── adapter/
│       └── persistence/     # Repository tests
└── api/
    └── controller/          # Controller tests (MockMvc)
```

### **Test Naming**
```java
// Unit tests
InfluencerProfileTest.java
ProfileManagementServiceTest.java

// Integration tests
InfluencerProfileRepositoryIntegrationTest.java
InfluencerProfileControllerIntegrationTest.java

// E2E tests
InfluencerProfileE2ETest.java
```

---

## **Kafka Topics Organization**

```
# User events
user.registered
user.updated

# Influencer events
influencer.profile.created
influencer.profile.updated
influencer.social-account.linked

# Social connector events
social.post.fetched
social.post.enriched
social.engagement.event

# Analytics events
analytics.kpi.updated
analytics.report.generated

# Campaign events
campaign.created
campaign.invite.sent
campaign.accepted
campaign.completed

# Payment events
payment.reserved
payment.captured
payment.refunded

# Notification events
notification.send
notification.sent
```

---

## **Docker Compose Services**

```yaml
# Core databases
postgres:5432
mongodb:27017
redis:6379

# Search & Graph
elasticsearch:9200
neo4j:7474, 7687

# Messaging
kafka:9092
zookeeper:2181
schema-registry:8081

# Observability
prometheus:9090
grafana:3000
zipkin:9411

# Development tools
kafka-ui:8090
mailhog:8025
```

---

## **Development Workflow**

### **1. Start Infrastructure**
```bash
docker-compose up -d postgres mongodb redis
```

### **2. Run Application**
```bash
./gradlew bootRun
```

### **3. Run Tests**
```bash
./gradlew test
./gradlew :modules:influencer-service:test
```

### **4. Build**
```bash
./gradlew clean build
```

### **5. Create Migration**
```sql
-- V{version}__{description}.sql
-- Example: V7__add_engagement_metrics.sql
```

---

## **Git Workflow**

### **Branch Naming**
```
feature/user-service-authentication
feature/influencer-profile-crud
feature/instagram-connector
bugfix/payment-webhook-handling
hotfix/security-vulnerability
```

### **Commit Messages**
```
feat(influencer): add profile management endpoints
fix(payment): handle stripe webhook timeout
refactor(analytics): optimize KPI calculation
docs(readme): update setup instructions
test(campaign): add integration tests
```

---

## **Documentation**

- **README.md** - Project overview, setup instructions
- **BACKEND_STRUCTURE.md** - Detailed architecture
- **roadmap.md** - Implementation roadmap
- **API.md** - API documentation (or use Swagger)
- **DEPLOYMENT.md** - Deployment guide

---

## **Useful Commands**

```bash
# Gradle
./gradlew tasks                    # List all tasks
./gradlew dependencies             # Show dependencies
./gradlew bootRun                  # Run application
./gradlew test                     # Run tests
./gradlew build                    # Build project

# Docker
docker-compose up -d               # Start all services
docker-compose ps                  # List services
docker-compose logs -f service     # View logs
docker-compose down                # Stop all services

# Database
docker exec -it influencenet-postgres psql -U influencenet_user -d influencenet
docker exec -it influencenet-mongodb mongosh -u admin -p admin_pass

# Kafka
docker exec -it influencenet-kafka kafka-topics --list --bootstrap-server localhost:9092
```

---

**Next**: Start implementing modules following this structure!
