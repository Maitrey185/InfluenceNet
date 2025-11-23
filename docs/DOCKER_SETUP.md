# Docker Setup Guide for InfluenceNet

This guide explains how to set up and use the local development environment using Docker Compose.

## Prerequisites

- Docker Desktop for Windows (version 20.10+)
- Docker Compose (included with Docker Desktop)
- At least 8GB RAM allocated to Docker
- At least 20GB free disk space

## Quick Start

1. **Start all services**:
   ```bash
   docker-compose up -d
   ```

2. **Check service health**:
   ```bash
   docker-compose ps
   ```

3. **View logs**:
   ```bash
   # All services
   docker-compose logs -f

   # Specific service
   docker-compose logs -f postgres
   ```

4. **Stop all services**:
   ```bash
   docker-compose down
   ```

5. **Stop and remove volumes** (clean slate):
   ```bash
   docker-compose down -v
   ```

## Services Overview

### Core Databases

| Service | Port | Credentials | Purpose |
|---------|------|-------------|---------|
| PostgreSQL | 5432 | user: `influencenet_user`<br>pass: `influencenet_pass` | Transactional data |
| MongoDB | 27017 | user: `admin`<br>pass: `admin_pass` | Social media raw data |
| Redis | 6379 | pass: `redis_pass` | Caching & sessions |

### Message Queue

| Service | Port | Purpose |
|---------|------|---------|
| Kafka | 9092 | Event streaming |
| Zookeeper | 2181 | Kafka coordination |
| Schema Registry | 8081 | Kafka schema management |
| Kafka UI | 8080 | Kafka management UI |

### Search & Graph

| Service | Port | Credentials | Purpose |
|---------|------|-------------|---------|
| Elasticsearch | 9200 | No auth | Full-text search |
| Kibana | 5601 | No auth | Elasticsearch UI |
| Neo4j | 7474 (HTTP)<br>7687 (Bolt) | user: `neo4j`<br>pass: `neo4j_password` | Graph database |

### Storage & Auth

| Service | Port | Credentials | Purpose |
|---------|------|-------------|---------|
| MinIO | 9000 (API)<br>9001 (Console) | user: `minioadmin`<br>pass: `minioadmin` | S3-compatible storage |
| Keycloak | 8180 | user: `admin`<br>pass: `admin` | Authentication |

### Observability

| Service | Port | Credentials | Purpose |
|---------|------|-------------|---------|
| Zipkin | 9411 | No auth | Distributed tracing |
| Prometheus | 9090 | No auth | Metrics collection |
| Grafana | 3000 | user: `admin`<br>pass: `admin` | Metrics visualization |

### Development Tools

| Service | Port | Purpose |
|---------|------|---------|
| Mailhog | 1025 (SMTP)<br>8025 (UI) | Email testing |

## Service URLs

After starting Docker Compose, access services at:

- **Kafka UI**: http://localhost:8080
- **Kibana**: http://localhost:5601
- **Neo4j Browser**: http://localhost:7474
- **MinIO Console**: http://localhost:9001
- **Keycloak Admin**: http://localhost:8180
- **Zipkin**: http://localhost:9411
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000
- **Mailhog UI**: http://localhost:8025

## Configuration

### Environment Variables

Copy `.env.example` to `.env` and customize as needed:

```bash
cp .env.example .env
```

### Application Properties

Update `src/main/resources/application.properties`:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/influencenet
spring.datasource.username=influencenet_user
spring.datasource.password=influencenet_pass
spring.datasource.hikari.maximum-pool-size=10

# MongoDB
spring.data.mongodb.uri=mongodb://admin:admin_pass@localhost:27017/influencenet?authSource=admin

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=redis_pass

# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Elasticsearch
spring.elasticsearch.uris=http://localhost:9200

# Neo4j
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=neo4j_password
```

## Initialization Scripts

### PostgreSQL

Scripts in `init-scripts/postgres/` run automatically on first startup:
- `01-init-databases.sql` - Creates databases and extensions

### MongoDB

Scripts in `init-scripts/mongodb/` run automatically on first startup:
- `01-init-collections.js` - Creates collections and indexes

## Common Commands

### Start specific services

```bash
docker-compose up -d postgres mongodb redis
```

### Restart a service

```bash
docker-compose restart kafka
```

### Execute commands in containers

```bash
# PostgreSQL
docker-compose exec postgres psql -U influencenet_user -d influencenet

# MongoDB
docker-compose exec mongodb mongosh -u admin -p admin_pass

# Redis
docker-compose exec redis redis-cli -a redis_pass
```

### View resource usage

```bash
docker stats
```

## Troubleshooting

### Services won't start

1. Check Docker Desktop is running
2. Ensure ports are not already in use
3. Check Docker logs: `docker-compose logs [service-name]`

### Out of memory

Increase Docker Desktop memory allocation:
- Docker Desktop → Settings → Resources → Memory (recommend 8GB+)

### Kafka connection issues

Wait for all health checks to pass:
```bash
docker-compose ps
```

Kafka depends on Zookeeper - ensure Zookeeper is healthy first.

### Reset everything

```bash
docker-compose down -v
docker-compose up -d
```

## Production Considerations

This Docker Compose setup is for **local development only**. For production:

- Use managed services (AWS RDS, MongoDB Atlas, etc.)
- Implement proper security (strong passwords, TLS/SSL)
- Configure proper resource limits
- Set up monitoring and alerting
- Use Kubernetes for orchestration
- Implement backup strategies

## Health Checks

All services include health checks. Monitor with:

```bash
docker-compose ps
```

Healthy services show `(healthy)` status.

## Next Steps

1. Start the infrastructure: `docker-compose up -d`
2. Wait for all services to be healthy
3. Run database migrations (Flyway)
4. Start your Spring Boot application
5. Access service UIs to verify connectivity

## Support

For issues or questions, refer to:
- Docker Compose documentation: https://docs.docker.com/compose/
- Individual service documentation (links in readme.md)
