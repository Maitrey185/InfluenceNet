# InfluenceNet Configuration Guide

## Overview
This document explains all dependencies and configuration properties added to connect with infrastructure services.

---

## Dependencies Added (build.gradle.kts)

### Core Spring Boot Starters
| Dependency | Purpose |
|------------|---------|
| `spring-boot-starter-web` | REST API development |
| `spring-boot-starter-data-jpa` | PostgreSQL integration with JPA/Hibernate |
| `spring-boot-starter-data-mongodb` | MongoDB integration |
| `spring-boot-starter-data-redis` | Redis caching |
| `spring-boot-starter-data-neo4j` | Neo4j graph database |
| `spring-boot-starter-data-elasticsearch` | Elasticsearch search |
| `spring-boot-starter-validation` | Bean validation |
| `spring-boot-starter-actuator` | Health checks & metrics |
| `spring-boot-starter-cache` | Caching abstraction |

### Messaging & Events
| Dependency | Purpose |
|------------|---------|
| `spring-kafka` | Kafka producer/consumer |

### Database Drivers & Tools
| Dependency | Purpose |
|------------|---------|
| `postgresql` | PostgreSQL JDBC driver |
| `flyway-core` | Database migrations |
| `flyway-database-postgresql` | PostgreSQL-specific Flyway support |
| `lettuce-core` | Redis client (async) |

### Observability
| Dependency | Purpose |
|------------|---------|
| `micrometer-registry-prometheus` | Prometheus metrics export |
| `micrometer-tracing-bridge-brave` | Distributed tracing |
| `zipkin-reporter-brave` | Zipkin integration |

### Storage & Utilities
| Dependency | Purpose |
|------------|---------|
| `aws-sdk-s3` | S3/MinIO file storage |
| `jackson-databind` | JSON processing |
| `jackson-datatype-jsr310` | Java 8 date/time support |
| `commons-lang3` | Utility functions |
| `lombok` | Boilerplate code reduction |

### Testing
| Dependency | Purpose |
|------------|---------|
| `spring-boot-starter-test` | Testing framework |
| `spring-kafka-test` | Kafka testing utilities |
| `testcontainers` | Integration testing with Docker |

---

## Configuration Properties (application.properties)

### PostgreSQL Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/influencenet
spring.datasource.username=influencenet_user
spring.datasource.password=influencenet_pass
```

**HikariCP Connection Pool**:
- Max pool size: 10 connections
- Min idle: 5 connections
- Connection timeout: 30 seconds

**JPA/Hibernate**:
- DDL auto: `validate` (Flyway handles schema)
- Batch size: 20 for performance
- Open-in-view: disabled (best practice)

**Flyway**:
- Enabled with baseline on migrate
- Migration scripts location: `classpath:db/migration`

### MongoDB Configuration
```properties
spring.data.mongodb.uri=mongodb://admin:admin_pass@localhost:27017/influencenet?authSource=admin
```

**Features**:
- Auto-index creation enabled
- Stores raw social media data and enriched posts

### Redis Configuration
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=redis_pass
```

**Connection Pool**:
- Max active: 8
- Max idle: 8
- Min idle: 2

**Cache Settings**:
- TTL: 1 hour (3600000ms)
- Null values: not cached

### Kafka Configuration
```properties
spring.kafka.bootstrap-servers=localhost:9092
```

**Producer**:
- Serializer: JSON
- Acks: all (strongest durability)
- Retries: 3
- Idempotence: enabled

**Consumer**:
- Group ID: `influencenet-consumer-group`
- Deserializer: JSON
- Auto-offset-reset: earliest
- Manual commit mode
- Concurrency: 3 threads

**Topics** (custom properties):
- `post.fetched`
- `post.enriched`
- `engagement.event`
- `kpi.updated`
- `campaign.events`
- `deal.saga`
- `notification.send`
- `report.requested`
- `report.ready`

### Elasticsearch Configuration
```properties
spring.elasticsearch.uris=http://localhost:9200
```

**Timeouts**:
- Connection: 5 seconds
- Socket: 60 seconds

### Neo4j Configuration
```properties
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=neo4j_password
```

**Connection Pool**:
- Max pool size: 50
- Acquisition timeout: 60 seconds

### Actuator & Monitoring
```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

**Exposed Endpoints**:
- `/actuator/health` - Health checks for all services
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus-formatted metrics

**Health Checks Enabled**:
- PostgreSQL
- MongoDB
- Redis
- Elasticsearch
- Neo4j

### Distributed Tracing (Zipkin)
```properties
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans
```

- 100% trace sampling (for development)
- Sends traces to Zipkin at localhost:9411

### MinIO/S3 Configuration
```properties
app.storage.endpoint=http://localhost:9000
app.storage.access-key=minioadmin
app.storage.secret-key=minioadmin
app.storage.bucket=influencenet-files
```

### Email Configuration (Mailhog)
```properties
spring.mail.host=localhost
spring.mail.port=1025
app.mail.from=noreply@influencenet.local
```

- Uses Mailhog for local email testing
- No authentication required

### Logging Configuration
```properties
logging.level.com.project.InfluenceNet=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

**Log Levels**:
- Application: DEBUG
- Spring Web: INFO
- MongoDB: DEBUG
- Kafka: INFO
- Hibernate SQL: DEBUG

---

## Database Schema

### Initial Migration: V1__initial_schema.sql

Created tables:
1. **users** - User accounts with roles
2. **influencer_profiles** - Influencer details
3. **social_accounts** - Connected social media accounts
4. **brand_profiles** - Brand company information
5. **brand_team_members** - Brand team management
6. **influencer_kpis** - Daily analytics metrics
7. **post_analytics** - Individual post performance
8. **posting_schedules** - Content scheduling
9. **campaigns** - Campaign management
10. **campaign_influencers** - Campaign-influencer relationships
11. **campaign_posts** - Posts linked to campaigns
12. **payments** - Payment transactions
13. **saga_executions** - Saga orchestration state
14. **reports** - Generated reports
15. **notifications** - User notifications
16. **file_metadata** - Uploaded files tracking
17. **audit_logs** - Audit trail

**Features**:
- UUID primary keys
- Automatic `updated_at` triggers
- Foreign key constraints
- Indexes for performance
- JSONB for flexible data

---

## Testing the Configuration

### 1. Start Infrastructure
```bash
docker-compose up -d
```

### 2. Build the Application
```bash
./gradlew build
```

### 3. Run the Application
```bash
./gradlew bootRun
```

### 4. Check Health
```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "mongo": {"status": "UP"},
    "redis": {"status": "UP"},
    "elasticsearch": {"status": "UP"},
    "neo4j": {"status": "UP"}
  }
}
```

### 5. Check Metrics
```bash
curl http://localhost:8080/actuator/prometheus
```

---

## Environment-Specific Configuration

### Development (application-dev.properties)
Create for development-specific overrides:
```properties
logging.level.root=DEBUG
spring.jpa.show-sql=true
```

### Production (application-prod.properties)
Create for production settings:
```properties
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
management.endpoints.web.exposure.include=health,info
```

Activate with:
```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## External Configuration (Environment Variables)

Sensitive values can be overridden via environment variables:

```bash
export INSTAGRAM_CLIENT_ID=your_client_id
export INSTAGRAM_CLIENT_SECRET=your_secret
export STRIPE_API_KEY=sk_test_xxx
export STRIPE_WEBHOOK_SECRET=whsec_xxx
```

These override the placeholder values in `application.properties`.

---

## Troubleshooting

### Connection Issues

**PostgreSQL**:
```bash
docker exec -it influencenet-postgres psql -U influencenet_user -d influencenet
```

**MongoDB**:
```bash
docker exec -it influencenet-mongodb mongosh -u admin -p admin_pass
```

**Redis**:
```bash
docker exec -it influencenet-redis redis-cli -a redis_pass
```

**Kafka**:
- Check Kafka UI: http://localhost:8080
- View topics and messages

### Flyway Migration Errors

If migration fails:
```bash
# Connect to PostgreSQL
docker exec -it influencenet-postgres psql -U influencenet_user -d influencenet

# Check Flyway schema history
SELECT * FROM flyway_schema_history;

# If needed, repair
./gradlew flywayRepair
```

### Health Check Failures

Check individual service health:
```bash
curl http://localhost:8080/actuator/health/db
curl http://localhost:8080/actuator/health/mongo
curl http://localhost:8080/actuator/health/redis
```

---

## Next Steps

1. **Create Entity Classes**: Map to database tables
2. **Create Repositories**: Spring Data JPA/MongoDB repositories
3. **Create Services**: Business logic layer
4. **Create Controllers**: REST API endpoints
5. **Add Security**: Spring Security + Keycloak integration
6. **Add Tests**: Unit and integration tests

---

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Kafka](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
