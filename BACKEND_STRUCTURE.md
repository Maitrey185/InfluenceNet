# InfluenceNet Backend Architecture

## **Modular Monolith → Microservices Ready Structure**

This document outlines a **domain-driven, modular backend architecture** that starts as a monolith but can easily be split into microservices.

---

## **Core Modules (Bounded Contexts)**

### **1. User Service** - Authentication & User Management
- User registration, login, profile management
- Role-based access control (INFLUENCER, BRAND, ADMIN)
- OAuth2/OIDC integration with Keycloak
- Password management

### **2. Influencer Service** - Influencer Profile & Growth
- Influencer profile management
- Social account linking (Instagram, YouTube, TikTok)
- Posting schedule & reminders
- Profile analytics dashboard

### **3. Social Connector Service** - Social Media Integration
- Instagram, YouTube, TikTok API integration
- OAuth flows for each platform
- Data fetching (posts, insights, followers)
- Webhook handlers for real-time updates
- Rate limiting & retry logic

### **4. Enrichment Service** - Data Processing
- Hashtag extraction
- Sentiment analysis
- Language detection
- Media metadata extraction
- Publish enriched data events

### **5. Analytics Service** - KPI & Metrics
- Daily KPI calculation
- Engagement analysis
- Growth tracking
- Heatmap generation
- Report generation

### **6. Campaign Service** - Campaign Management
- Campaign creation & management
- Influencer invitations
- Campaign tracking
- Performance analytics
- Saga orchestration for deals

### **7. Brand Service** - Brand Profile Management
- Brand profile CRUD
- Team member management
- Billing information

### **8. Payment Service** - Payment Processing
- Stripe integration
- Payment reserve/capture/refund
- Webhook handling
- Transaction history

### **9. Search Service** - Search & Discovery
- Elasticsearch integration
- Influencer search
- Post search
- Hashtag search
- Auto-complete

### **10. Graph Service** - Relationship Network
- Neo4j integration
- Collaboration network
- Influencer discovery
- Network analysis

### **11. Recommendation Service** - AI Recommendations
- Content recommendations
- Collaboration suggestions
- Posting schedule optimization
- Rule-based & ML-based

### **12. Notification Service** - Notifications
- Email notifications
- SMS (future)
- Push notifications (future)
- Notification history

---

## **Project Structure**

```
InfluenceNet/
├── common/                    # Shared libraries
│   ├── common-domain/        # Domain models, events, exceptions
│   ├── common-infrastructure/# Config, security, messaging, persistence
│   └── common-application/   # DTOs, mappers, validators
│
├── modules/                   # Business modules
│   ├── user-service/
│   ├── influencer-service/
│   ├── social-connector-service/
│   ├── enrichment-service/
│   ├── analytics-service/
│   ├── campaign-service/
│   ├── brand-service/
│   ├── payment-service/
│   ├── search-service/
│   ├── graph-service/
│   ├── recommendation-service/
│   └── notification-service/
│
├── gateway/                   # API Gateway
└── bootstrap/                 # Monolith runner
```

### **Each Module Structure**
```
module-name-service/
├── module-domain/            # Entities, repositories, domain services
├── module-application/       # Use cases, DTOs, orchestration
├── module-infrastructure/    # DB, Kafka, external APIs
└── module-api/              # REST controllers
```

---

## **Architecture Principles**

1. **Hexagonal Architecture** - Clean separation of concerns
2. **Domain-Driven Design** - Bounded contexts per module
3. **Event-Driven** - Kafka for inter-module communication
4. **Database Per Module** - Each module owns its data
5. **API Gateway Pattern** - Single entry point

---

## **Technology Stack**

- **Backend**: Spring Boot 3.5.7, Java 17
- **Databases**: Postgres, MongoDB, Redis, Neo4j, Elasticsearch
- **Messaging**: Kafka 3.x
- **Security**: Keycloak, OAuth2, JWT
- **Observability**: Prometheus, Grafana, Zipkin
- **DevOps**: Docker, Kubernetes, Helm

---

## **Migration Path**

1. **Modular Monolith** (Current) - All modules in one JAR
2. **Distributed Monolith** - Extract gateway
3. **Selective Extraction** - Extract high-value services
4. **Full Microservices** - Independent services

---

See detailed structure in code repository.
