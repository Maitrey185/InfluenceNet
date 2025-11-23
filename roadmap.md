# InfluenceNet Backend Implementation Roadmap

**Note**: This roadmap focuses on **backend microservices and APIs**. See `FRONTEND_ROADMAP.md` for parallel frontend development.

## **Development Strategy**

- **Backend-First Approach**: APIs developed first, then consumed by frontend
- **Parallel Development**: Frontend team can start with mock APIs (MSW) while backend is in progress
- **API-First Design**: OpenAPI/Swagger specs created before implementation
- **Continuous Integration**: Each sprint delivers working, testable APIs

### **API Documentation Standards**

Every sprint must deliver:
1. **OpenAPI 3.0 Specification** (Swagger) for all endpoints
2. **Postman Collection** with example requests/responses
3. **API Documentation** (auto-generated from Swagger)
4. **Integration Tests** for all endpoints

**Tools**:
- SpringDoc OpenAPI (automatic Swagger generation)
- Swagger UI available at `/swagger-ui.html`
- API docs available at `/api-docs`

---

## **PHASE 0: FOUNDATION & INFRASTRUCTURE** (Sprints 1-3, ~6 weeks)

**Frontend Parallel Work**: Sprint 1-2 (Project setup, Design system, Auth UI)

### **Sprint 1: Core Infrastructure Setup** (Week 1-2)

**Goal**: Establish development environment and core infrastructure

**Tasks**:
1. **Project Structure & Build Setup**
    - ✅ Spring Boot 3.5.7 base (DONE)
    - Add multi-module Gradle structure for microservices
    - Configure shared libraries module (common DTOs, exceptions, utilities)
    - Setup code quality tools (Checkstyle, SpotBugs, JaCoCo for coverage)

2. **Database Infrastructure**
    - Docker Compose for local development (Postgres, MongoDB, Redis, Kafka)
    - Postgres 15+ setup with connection pooling (HikariCP)
    - MongoDB 6+ setup
    - Redis 7+ for caching
    - Database migration tool (Flyway or Liquibase)

3. **Kafka Event Bus**
    - Kafka 3.x setup (3 brokers for dev)
    - Schema Registry (Confluent or Apicurio)
    - Create initial topic structure (see Phase topics below)
    - Kafka admin utilities

4. **Observability Foundation**
    - Spring Boot Actuator
    - Micrometer + Prometheus metrics
    - Logback with JSON formatting
    - Distributed tracing setup (Micrometer Tracing + Zipkin)

**Deliverables**:
- `docker-compose.yml` with all infrastructure
- Multi-module Gradle project structure
- Basic health check endpoints
- Local development guide

---

### **Sprint 2: Authentication & API Gateway** (Week 3-4)

**Goal**: Secure authentication and routing layer

**Tasks**:
1. **Auth & Identity Service**
    - Keycloak 23+ setup in Docker
    - Realm configuration (InfluenceNet realm)
    - User roles: `INFLUENCER`, `BRAND`, `ADMIN`
    - OAuth2/OIDC integration with Spring Security
    - JWT token validation
    - User registration endpoints
    - Password reset flow

2. **API Gateway**
    - Spring Cloud Gateway 4.x
    - Route configuration for future services
    - Rate limiting (Redis-backed)
    - CORS configuration
    - Request/response logging filter
    - Circuit breaker (Resilience4j)

3. **User Profile Foundation**
    - User entity (Postgres): id, email, username, role, created_at, updated_at
    - User repository with Spring Data JPA
    - Profile CRUD endpoints
    - Password encryption (BCrypt)

**Database Schema**:
```sql
-- users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
```

**API Endpoints**:
- `POST /auth/register` - User registration
- `POST /auth/login` - Login (returns JWT)
- `GET /auth/me` - Current user info
- `POST /auth/refresh` - Refresh token
- `POST /auth/forgot-password`
- `POST /auth/reset-password`

**Deliverables**:
- Keycloak configured and running
- API Gateway routing requests
- Auth service with JWT validation
- **OpenAPI/Swagger documentation for auth endpoints**
- Postman collection for auth flows

**Frontend Integration**: Auth APIs ready for Frontend Sprint 2 (Auth UI)

---

### **Sprint 3: Shared Services & DevOps** (Week 5-6)

**Frontend Parallel Work**: Sprint 2 (Auth UI integration)

**Goal**: Common services and CI/CD pipeline

**Tasks**:
1. **Notification Service (Basic)**
    - Email sender (Spring Mail + SendGrid/AWS SES)
    - Notification templates (Thymeleaf)
    - Kafka consumer for `notification.send` topic
    - Notification types: email, SMS placeholder, push placeholder
    - Notification history (Postgres)

2. **File Storage Service**
    - AWS S3 integration (or MinIO for local)
    - Upload/download endpoints
    - Presigned URL generation
    - File metadata storage (Postgres)

3. **CI/CD Pipeline**
    - GitHub Actions workflows
    - Build: compile, test, code quality checks
    - Docker image build and push (to Docker Hub/ECR)
    - Kubernetes deployment manifests (Helm charts)
    - Dev environment auto-deployment

4. **Secrets Management**
    - HashiCorp Vault setup (or AWS Secrets Manager)
    - Spring Cloud Vault integration
    - Rotate database credentials
    - Store API keys (Stripe, social platforms)

**Kafka Topics Created**:
- `notification.send`
- `file.uploaded`

**Deliverables**:
- Notification service sending emails
- S3/MinIO file uploads working
- CI/CD pipeline deploying to dev K8s
- Vault storing secrets

---

## **PHASE 1: INFLUENCER GROWTH CORE** (Sprints 4-8, ~10 weeks)

### **Sprint 4: Influencer Profile Service** (Week 7-8)

**Goal**: Complete influencer profile management

**Tasks**:
1. **Influencer Profile Entity**
    - Extended profile (Postgres): bio, profile_pic_url, niche, location, timezone
    - Social accounts table: platform, account_id, username, access_token, refresh_token
    - Preferences: notification settings, privacy settings

2. **Profile Management APIs**
    - `GET /influencers/{id}` - Get profile
    - `PUT /influencers/{id}` - Update profile
    - `POST /influencers/{id}/social-accounts` - Link social account
    - `DELETE /influencers/{id}/social-accounts/{platform}` - Unlink
    - `GET /influencers/{id}/social-accounts` - List linked accounts

3. **OAuth Integration Prep**
    - OAuth2 client setup for Instagram, YouTube, TikTok
    - Callback endpoints for OAuth flow
    - Token storage (encrypted in Vault)

**Database Schema**:
```sql
CREATE TABLE influencer_profiles (
    id UUID PRIMARY KEY REFERENCES users(id),
    bio TEXT,
    profile_pic_url VARCHAR(500),
    niche VARCHAR(100),
    location VARCHAR(200),
    timezone VARCHAR(50),
    follower_count_total INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE social_accounts (
    id UUID PRIMARY KEY,
    influencer_id UUID REFERENCES influencer_profiles(id),
    platform VARCHAR(50) NOT NULL, -- 'instagram', 'youtube', 'tiktok', 'twitter'
    platform_user_id VARCHAR(255),
    username VARCHAR(255),
    access_token_ref VARCHAR(500), -- Vault reference
    refresh_token_ref VARCHAR(500),
    token_expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    connected_at TIMESTAMP DEFAULT NOW(),
    last_synced_at TIMESTAMP,
    UNIQUE(influencer_id, platform)
);
```

**Deliverables**:
- Influencer profile CRUD working
- Social account linking (OAuth flow ready)
- Profile dashboard UI mockup

---

### **Sprint 5: Social Connector Service - Instagram** (Week 9-11)

**Goal**: Fetch and store Instagram data

**Tasks**:
1. **Instagram Graph API Integration**
    - Instagram Basic Display API / Graph API setup
    - Fetch user media (posts, reels)
    - Fetch media insights (likes, comments, shares, saves)
    - Fetch follower count
    - Webhook setup for real-time updates

2. **Social Connector Service**
    - Polling scheduler (Spring @Scheduled, every 6 hours)
    - Instagram adapter implementing common interface
    - Rate limit handling (429 responses)
    - Retry logic with exponential backoff
    - Store raw payloads in MongoDB

3. **Data Models**
    - MongoDB collections: `raw_posts`, `raw_insights`
    - Normalized post structure (common across platforms)

**MongoDB Schema**:
```json
// raw_posts collection
{
  "_id": "ObjectId",
  "influencer_id": "UUID",
  "platform": "instagram",
  "platform_post_id": "string",
  "raw_payload": {}, // Full API response
  "fetched_at": "ISODate",
  "post_type": "image|video|carousel",
  "caption": "string",
  "media_url": "string",
  "permalink": "string",
  "timestamp": "ISODate"
}

// raw_insights collection
{
  "_id": "ObjectId",
  "post_id": "string",
  "platform": "instagram",
  "likes": 0,
  "comments": 0,
  "shares": 0,
  "saves": 0,
  "reach": 0,
  "impressions": 0,
  "fetched_at": "ISODate"
}
```

4. **Event Publishing**
    - Publish `post.fetched` event to Kafka
    - Event schema: influencer_id, platform, post_id, timestamp

**Kafka Topics**:
- `post.fetched`
- `engagement.event`

**Deliverables**:
- Instagram posts fetching every 6 hours
- Raw data stored in MongoDB
- Events published to Kafka
- Admin endpoint to trigger manual sync

---

### **Sprint 6: Ingestion Enricher & Analytics Foundation** (Week 12-13)

**Goal**: Enrich posts and compute basic analytics

**Tasks**:
1. **Ingestion Enricher Service**
    - Kafka consumer for `post.fetched`
    - Hashtag extraction (regex-based)
    - Language detection (Apache Tika or external API)
    - Sentiment analysis (rule-based: positive/negative/neutral word lists)
    - Media metadata extraction (dimensions, duration for videos)
    - Store enriched data in MongoDB
    - Publish `post.enriched` event

**Enriched Post Schema**:
```json
{
  "_id": "ObjectId",
  "post_id": "string",
  "influencer_id": "UUID",
  "platform": "instagram",
  "enrichments": {
    "hashtags": ["fitness", "motivation"],
    "mentions": ["@brand"],
    "language": "en",
    "sentiment": "positive",
    "media_type": "video",
    "media_metadata": {
      "duration_seconds": 30,
      "width": 1080,
      "height": 1920
    }
  },
  "enriched_at": "ISODate"
}
```

2. **Analytics Service - Foundation**
    - Kafka consumer for `post.enriched` and `engagement.event`
    - Compute daily KPIs per influencer
    - Aggregate metrics: total posts, avg likes, avg comments, engagement rate
    - Store in Postgres (time-series optimized table)

**Database Schema**:
```sql
CREATE TABLE influencer_kpis (
    id UUID PRIMARY KEY,
    influencer_id UUID REFERENCES influencer_profiles(id),
    platform VARCHAR(50),
    date DATE NOT NULL,
    posts_count INT DEFAULT 0,
    total_likes INT DEFAULT 0,
    total_comments INT DEFAULT 0,
    total_shares INT DEFAULT 0,
    total_saves INT DEFAULT 0,
    avg_engagement_rate DECIMAL(5,2),
    follower_count INT,
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(influencer_id, platform, date)
);

CREATE TABLE post_analytics (
    id UUID PRIMARY KEY,
    post_id VARCHAR(255) NOT NULL,
    influencer_id UUID REFERENCES influencer_profiles(id),
    platform VARCHAR(50),
    posted_at TIMESTAMP,
    likes INT DEFAULT 0,
    comments INT DEFAULT 0,
    shares INT DEFAULT 0,
    saves INT DEFAULT 0,
    reach INT DEFAULT 0,
    impressions INT DEFAULT 0,
    engagement_rate DECIMAL(5,2),
    updated_at TIMESTAMP DEFAULT NOW()
);
```

**Deliverables**:
- Posts enriched with hashtags, sentiment
- Daily KPIs computed and stored
- `post.enriched` events flowing

---

### **Sprint 7: Analytics Dashboard APIs** (Week 14-15)

**Goal**: Expose analytics data via APIs

**Tasks**:
1. **Analytics Query APIs**
    - `GET /analytics/influencers/{id}/overview` - Summary stats
    - `GET /analytics/influencers/{id}/kpis?start_date&end_date&platform` - Time-series KPIs
    - `GET /analytics/influencers/{id}/posts?sort=engagement&limit=10` - Top posts
    - `GET /analytics/influencers/{id}/growth` - Follower growth curve
    - `GET /analytics/influencers/{id}/engagement-heatmap` - Best posting times

2. **Engagement Heatmap Calculation**
    - Aggregate engagement by hour of day and day of week
    - Store in Redis cache (TTL 24 hours)
    - Return heatmap matrix (7x24)

3. **Caching Strategy**
    - Redis cache for frequently accessed analytics
    - Cache invalidation on new data ingestion
    - Cache warming for active influencers

**API Response Examples**:
```json
// GET /analytics/influencers/{id}/overview
{
  "influencer_id": "uuid",
  "total_posts": 150,
  "total_followers": 50000,
  "avg_engagement_rate": 4.5,
  "platforms": {
    "instagram": {
      "posts": 150,
      "followers": 50000,
      "avg_likes": 2250,
      "avg_comments": 150
    }
  },
  "period": "last_30_days"
}
```

**Deliverables**:
- Analytics APIs returning real data
- Engagement heatmap working
- Redis caching implemented

---

### **Sprint 8: Scheduler & Reminder Service** (Week 16-17)

**Goal**: Posting schedule and reminders

**Tasks**:
1. **Scheduler Service**
    - Schedule entity (Postgres): influencer_id, scheduled_time, content_draft, platform, status
    - CRUD APIs for schedules
    - Reminder job (Spring @Scheduled every 15 min)
    - Send notification 1 hour before scheduled time
    - Publish `reminder.sent` event

**Database Schema**:
```sql
CREATE TABLE posting_schedules (
    id UUID PRIMARY KEY,
    influencer_id UUID REFERENCES influencer_profiles(id),
    platform VARCHAR(50),
    scheduled_time TIMESTAMP NOT NULL,
    content_draft TEXT,
    media_urls TEXT[], -- Array of URLs
    status VARCHAR(50) DEFAULT 'pending', -- pending, reminded, posted, cancelled
    reminder_sent BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
```

2. **Schedule Management APIs**
    - `POST /schedule` - Create schedule
    - `GET /schedule/{influencerId}` - List schedules
    - `PUT /schedule/{id}` - Update schedule
    - `DELETE /schedule/{id}` - Cancel schedule
    - `POST /schedule/{id}/mark-posted` - Mark as posted

3. **Reminder Logic**
    - Query schedules where `scheduled_time - 1 hour < NOW() AND reminder_sent = false`
    - Send notification via Notification Service
    - Update `reminder_sent = true`

**Deliverables**:
- Scheduling system working
- Reminders sent 1 hour before
- Integration with Notification Service

---

## **PHASE 2: BRAND & CAMPAIGNS** (Sprints 9-13, ~10 weeks)

### **Sprint 9: Brand Profile Service** (Week 18-19)

**Goal**: Brand profile management

**Tasks**:
1. **Brand Profile Entity**
    - Brand profile (Postgres): company_name, industry, website, logo_url
    - Billing info: billing_email, payment_method_id (Stripe)
    - Team members: brand_users table (many-to-many with users)

**Database Schema**:
```sql
CREATE TABLE brand_profiles (
    id UUID PRIMARY KEY REFERENCES users(id),
    company_name VARCHAR(255) NOT NULL,
    industry VARCHAR(100),
    website VARCHAR(500),
    logo_url VARCHAR(500),
    billing_email VARCHAR(255),
    stripe_customer_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE brand_team_members (
    id UUID PRIMARY KEY,
    brand_id UUID REFERENCES brand_profiles(id),
    user_id UUID REFERENCES users(id),
    role VARCHAR(50), -- owner, admin, member
    invited_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(brand_id, user_id)
);
```

2. **Brand Management APIs**
    - `GET /brands/{id}` - Get brand profile
    - `PUT /brands/{id}` - Update brand
    - `POST /brands/{id}/team` - Invite team member
    - `DELETE /brands/{id}/team/{userId}` - Remove member
    - `GET /brands/{id}/team` - List team

**Deliverables**:
- Brand profile CRUD working
- Team management functional

---

### **Sprint 10: Campaign Service - Core** (Week 20-22)

**Goal**: Campaign creation and management

**Tasks**:
1. **Campaign Entity**
    - Campaign (Postgres): brand_id, name, description, budget, start_date, end_date, status
    - Campaign goals: impressions_target, engagement_target, conversions_target
    - Targeting criteria: niche, follower_range, location, platforms

**Database Schema**:
```sql
CREATE TABLE campaigns (
    id UUID PRIMARY KEY,
    brand_id UUID REFERENCES brand_profiles(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    budget_total DECIMAL(10,2),
    budget_spent DECIMAL(10,2) DEFAULT 0,
    start_date DATE,
    end_date DATE,
    status VARCHAR(50) DEFAULT 'draft', -- draft, active, paused, completed, cancelled
    targeting_criteria JSONB, -- Flexible targeting rules
    goals JSONB, -- impressions, engagement, conversions targets
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE campaign_influencers (
    id UUID PRIMARY KEY,
    campaign_id UUID REFERENCES campaigns(id),
    influencer_id UUID REFERENCES influencer_profiles(id),
    status VARCHAR(50) DEFAULT 'invited', -- invited, accepted, rejected, completed
    offered_amount DECIMAL(10,2),
    negotiated_amount DECIMAL(10,2),
    deliverables JSONB, -- posts, stories, reels count
    invited_at TIMESTAMP DEFAULT NOW(),
    accepted_at TIMESTAMP,
    completed_at TIMESTAMP,
    UNIQUE(campaign_id, influencer_id)
);
```

2. **Campaign APIs**
    - `POST /campaigns` - Create campaign
    - `GET /campaigns/{id}` - Get campaign details
    - `PUT /campaigns/{id}` - Update campaign
    - `DELETE /campaigns/{id}` - Delete campaign (soft delete)
    - `GET /brands/{brandId}/campaigns` - List brand campaigns
    - `POST /campaigns/{id}/invite` - Invite influencer
    - `GET /campaigns/{id}/influencers` - List campaign influencers

3. **Event Publishing**
    - `campaign.created`
    - `campaign.invite.sent`

**Kafka Topics**:
- `campaign.events` (all campaign lifecycle events)

**Deliverables**:
- Campaign CRUD working
- Influencer invitation flow
- Events published

---

### **Sprint 11: Payments Service - Stripe Integration** (Week 23-24)

**Goal**: Payment processing with Stripe

**Tasks**:
1. **Stripe Setup**
    - Stripe SDK integration (Spring Boot)
    - Webhook endpoint for Stripe events
    - Customer creation on brand registration
    - Payment method attachment

2. **Payment Service**
    - Payment entity (Postgres): campaign_id, influencer_id, amount, status, stripe_payment_intent_id
    - Reserve funds (create PaymentIntent)
    - Capture payment (capture PaymentIntent)
    - Refund payment

**Database Schema**:
```sql
CREATE TABLE payments (
    id UUID PRIMARY KEY,
    campaign_id UUID REFERENCES campaigns(id),
    influencer_id UUID REFERENCES influencer_profiles(id),
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status VARCHAR(50) DEFAULT 'pending', -- pending, reserved, captured, refunded, failed
    stripe_payment_intent_id VARCHAR(255),
    stripe_charge_id VARCHAR(255),
    reserved_at TIMESTAMP,
    captured_at TIMESTAMP,
    refunded_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
```

3. **Payment APIs**
    - `POST /payments/reserve` - Reserve funds (internal, called by Saga)
    - `POST /payments/capture` - Capture payment
    - `POST /payments/refund` - Refund payment
    - `GET /payments/{id}` - Payment status
    - `GET /campaigns/{id}/payments` - Campaign payments

4. **Stripe Webhooks**
    - `payment_intent.succeeded`
    - `payment_intent.payment_failed`
    - `charge.refunded`

**Deliverables**:
- Stripe integration working (test mode)
- Payment reserve/capture flow
- Webhook handling

---

### **Sprint 12: Deals & Payments Orchestrator (Saga)** (Week 25-26)

**Goal**: Orchestrate campaign acceptance with payment

**Tasks**:
1. **Saga Orchestrator**
    - Use Temporal.io or custom saga implementation
    - Campaign acceptance saga workflow:
        1. Influencer accepts campaign
        2. Reserve payment (call Payment Service)
        3. If reserve succeeds → update campaign status to 'active'
        4. If reserve fails → rollback, notify brand
        5. Publish `campaign.started` event

2. **Saga State Machine**
    - Saga execution table (Postgres): saga_id, type, status, current_step, payload
    - Compensating transactions for rollback

**Database Schema**:
```sql
CREATE TABLE saga_executions (
    id UUID PRIMARY KEY,
    saga_type VARCHAR(100) NOT NULL, -- 'campaign_acceptance', 'campaign_completion'
    status VARCHAR(50) DEFAULT 'running', -- running, completed, failed, compensating
    current_step VARCHAR(100),
    payload JSONB,
    started_at TIMESTAMP DEFAULT NOW(),
    completed_at TIMESTAMP,
    error_message TEXT
);
```

3. **Campaign Acceptance Flow**
    - `POST /campaigns/{id}/accept` (influencer endpoint)
    - Trigger saga
    - Return saga_id for tracking
    - Async completion notification

**Kafka Topics**:
- `deal.saga` (saga orchestration events)

**Deliverables**:
- Saga orchestrator working
- Campaign acceptance with payment reserve
- Rollback on failure

---

### **Sprint 13: Campaign Tracking & Reporting** (Week 27-28)

**Goal**: Track campaign performance

**Tasks**:
1. **Campaign Post Tracking**
    - Link posts to campaigns (via hashtag or manual tagging)
    - Campaign post entity: campaign_id, post_id, tagged_at
    - Aggregate campaign metrics from linked posts

**Database Schema**:
```sql
CREATE TABLE campaign_posts (
    id UUID PRIMARY KEY,
    campaign_id UUID REFERENCES campaigns(id),
    post_id VARCHAR(255) NOT NULL,
    influencer_id UUID REFERENCES influencer_profiles(id),
    platform VARCHAR(50),
    tagged_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(campaign_id, post_id)
);
```

2. **Campaign Analytics**
    - `GET /campaigns/{id}/analytics` - Campaign performance
    - Metrics: total impressions, total engagement, ROI calculation
    - Compare against goals

3. **Campaign Completion**
    - `POST /campaigns/{id}/complete` - Mark campaign complete
    - Trigger saga to capture payment
    - Generate final report
    - Publish `campaign.completed` event

**Deliverables**:
- Campaign tracking working
- Campaign analytics API
- Completion flow with payment capture

---

## **PHASE 3: SEARCH, GRAPH & RECOMMENDATIONS** (Sprints 14-18, ~10 weeks)

### **Sprint 14: Elasticsearch Integration** (Week 29-30)

**Goal**: Full-text search across platform

**Tasks**:
1. **Elasticsearch Setup**
    - Elasticsearch 8.x in Docker
    - Spring Data Elasticsearch
    - Index mappings for: influencers, posts, campaigns, hashtags

2. **Search & Indexing Service**
    - Kafka consumer for indexing events
    - Index influencer profiles on create/update
    - Index posts on `post.enriched`
    - Index campaigns on create/update

**Elasticsearch Mappings**:
```json
// influencers index
{
  "mappings": {
    "properties": {
      "id": {"type": "keyword"},
      "username": {"type": "text"},
      "bio": {"type": "text"},
      "niche": {"type": "keyword"},
      "location": {"type": "text"},
      "follower_count": {"type": "integer"},
      "avg_engagement_rate": {"type": "float"},
      "platforms": {"type": "keyword"}
    }
  }
}

// posts index
{
  "mappings": {
    "properties": {
      "post_id": {"type": "keyword"},
      "influencer_id": {"type": "keyword"},
      "caption": {"type": "text"},
      "hashtags": {"type": "keyword"},
      "platform": {"type": "keyword"},
      "posted_at": {"type": "date"},
      "likes": {"type": "integer"},
      "engagement_rate": {"type": "float"}
    }
  }
}
```

3. **Search APIs**
    - `GET /search/influencers?q={query}&niche={niche}&min_followers={n}` - Search influencers
    - `GET /search/posts?q={query}&hashtags={tags}&platform={platform}` - Search posts
    - `GET /search/hashtags?q={query}` - Search hashtags
    - `GET /search/campaigns?q={query}` - Search campaigns

**Deliverables**:
- Elasticsearch indexing working
- Search APIs returning results
- Autocomplete for hashtags

---

### **Sprint 15: Neo4j Graph Database** (Week 31-32)

**Goal**: Relationship graph for collaboration discovery

**Tasks**:
1. **Neo4j Setup**
    - Neo4j 5.x in Docker (or Neo4j Aura)
    - Spring Data Neo4j
    - Graph schema: Influencer nodes, Brand nodes, relationships

2. **Graph Service**
    - Create influencer nodes on profile creation
    - Create brand nodes on brand creation
    - Create relationships:
        - `COLLABORATED_WITH` (influencer → influencer)
        - `WORKED_WITH` (influencer → brand)
        - `FOLLOWS` (influencer → influencer, from social data)
        - `TAGGED_IN` (influencer → post)

**Graph Schema**:
```cypher
// Nodes
(:Influencer {id, username, niche, follower_count})
(:Brand {id, company_name, industry})
(:Post {id, platform, posted_at})

// Relationships
(:Influencer)-[:COLLABORATED_WITH {campaign_id, date}]->(:Influencer)
(:Influencer)-[:WORKED_WITH {campaign_id, amount, date}]->(:Brand)
(:Influencer)-[:FOLLOWS]->(:Influencer)
(:Influencer)-[:CREATED {date}]->(:Post)
```

3. **Graph Population**
    - Kafka consumer for `campaign.completed` → create WORKED_WITH relationship
    - Detect collaborations from post mentions → create COLLABORATED_WITH
    - Batch job to import existing data

4. **Graph Query APIs**
    - `GET /graph/influencers/{id}/network?depth=2` - Get collaboration network
    - `GET /graph/influencers/{id}/collaborators` - Direct collaborators
    - `GET /graph/brands/{id}/influencers` - Influencers worked with brand

**Deliverables**:
- Neo4j graph populated
- Graph query APIs working
- Collaboration network visualization data

---

### **Sprint 16: Recommendation Engine v1 (Rule-Based)** (Week 33-35)

**Goal**: Rule-based recommendations for influencers

**Tasks**:
1. **Recommendation Service**
    - Rule-based recommendation engine
    - Recommendation types: content, collaboration, posting schedule

2. **Content Recommendations**
    - Analyze top-performing posts (by engagement rate)
    - Identify patterns: post type (video vs image), hashtags, posting time
    - Generate recommendations:
        - "Your videos get 30% more engagement than photos"
        - "Posts with #fitness hashtag perform 20% better"
        - "Your audience engages most on Sundays at 7 PM"

3. **Collaboration Recommendations**
    - Graph queries to find similar influencers (same niche, similar follower count)
    - Find influencers who collaborated with same brands
    - Score candidates by: niche match, follower overlap, engagement similarity

4. **Posting Schedule Recommendations**
    - Analyze engagement heatmap
    - Recommend top 3 time slots per week
    - Suggest posting frequency based on historical data

**Recommendation APIs**:
- `GET /recommendations/{influencerId}?type=content` - Content recommendations
- `GET /recommendations/{influencerId}?type=collab` - Collaboration suggestions
- `GET /recommendations/{influencerId}?type=schedule` - Posting schedule suggestions

**Deliverables**:
- Rule-based recommendation engine
- Recommendations displayed in dashboard
- Weekly recommendation emails

---

### **Sprint 17: Multi-Platform Connectors** (Week 36-38)

**Goal**: Add YouTube and TikTok connectors

**Tasks**:
1. **YouTube Connector**
    - YouTube Data API v3 integration
    - OAuth flow for YouTube
    - Fetch videos, shorts
    - Fetch video analytics (views, likes, comments)
    - Polling scheduler

2. **TikTok Connector**
    - TikTok API integration (requires business account)
    - OAuth flow for TikTok
    - Fetch videos
    - Fetch video analytics
    - Polling scheduler

3. **Connector Abstraction**
    - Common interface: `SocialConnector`
    - Platform-specific adapters: `InstagramAdapter`, `YouTubeAdapter`, `TikTokAdapter`
    - Factory pattern for connector selection

**Interface Design**:
```java
public interface SocialConnector {
    List<Post> fetchPosts(String userId, LocalDate since);
    PostInsights fetchInsights(String postId);
    UserProfile fetchProfile(String userId);
    void refreshToken(String userId);
}
```

**Deliverables**:
- YouTube connector working
- TikTok connector working
- Multi-platform analytics aggregation

---

### **Sprint 18: Reporting Service** (Week 39-40)

**Goal**: Automated report generation

**Tasks**:
1. **Reporting Service**
    - Report templates (HTML/PDF)
    - Report types: weekly influencer report, campaign report
    - Async report generation (Kafka-based)
    - Store reports in S3

2. **Weekly Influencer Report**
    - Summary: posts count, engagement rate, follower growth
    - Top posts of the week
    - Recommendations
    - Generated every Monday morning (cron job)

3. **Campaign Report**
    - Campaign summary: budget, influencers, posts
    - Performance metrics: impressions, engagement, ROI
    - Influencer breakdown
    - Generated on campaign completion

**Database Schema**:
```sql
CREATE TABLE reports (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL, -- 'weekly_influencer', 'campaign'
    entity_id UUID NOT NULL, -- influencer_id or campaign_id
    report_url VARCHAR(500), -- S3 URL
    status VARCHAR(50) DEFAULT 'pending', -- pending, generating, completed, failed
    generated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW()
);
```

**Reporting APIs**:

I'll create a concise roadmap document in your workspace instead of outputting it all here.
