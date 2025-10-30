# Docker Health Check Fixes

## Summary
Fixed health checks for Elasticsearch, MongoDB, Neo4j, and Keycloak to properly report container health status.

## Changes Made

### 1. Elasticsearch
**Issue**: Health check was timing out during startup
**Fix**: 
- Added `start_period: 60s` to allow more startup time
- Increased interval to 15s and timeout to 10s
- Changed to check for both "green" and "yellow" cluster status
- Increased retries to 10

```yaml
healthcheck:
  test: ["CMD-SHELL", "curl -s http://localhost:9200/_cluster/health | grep -q '\"status\":\"green\"\\|\"status\":\"yellow\"' || exit 1"]
  interval: 15s
  timeout: 10s
  retries: 10
  start_period: 60s
```

### 2. MongoDB
**Issue**: Health check command format was incorrect
**Fix**:
- Changed from shell pipe to proper CMD array format
- Added `start_period: 40s` for initialization time
- Increased interval and timeout

```yaml
healthcheck:
  test: ["CMD", "mongosh", "--eval", "db.adminCommand('ping')", "--quiet"]
  interval: 15s
  timeout: 10s
  retries: 5
  start_period: 40s
```

### 3. Neo4j
**Issue**: Health check was too aggressive during startup
**Fix**:
- Added `start_period: 60s` for plugin loading (APOC, GDS)
- Increased interval to 15s and timeout to 10s
- Increased retries to 10

```yaml
healthcheck:
  test: ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:7474 || exit 1"]
  interval: 15s
  timeout: 10s
  retries: 10
  start_period: 60s
```

### 4. Keycloak
**Issue**: Container doesn't have `curl` installed
**Fix**:
- Changed to use `/dev/tcp` for TCP port check (bash built-in)
- Added `start_period: 120s` (Keycloak takes ~2 minutes to start)
- Increased retries to 15
- Simplified to TCP connection test to avoid YAML escape issues

```yaml
healthcheck:
  test: ["CMD-SHELL", "exec 3<>/dev/tcp/localhost/8080 || exit 1"]
  interval: 15s
  timeout: 10s
  retries: 15
  start_period: 120s
```

### 5. Removed Obsolete Version
**Issue**: Docker Compose warning about obsolete `version` attribute
**Fix**: Removed `version: '3.8'` line (not needed in modern Docker Compose)

## Health Check Parameters Explained

- **test**: Command to run to check health
- **interval**: Time between health checks (after container is running)
- **timeout**: Maximum time to wait for health check to complete
- **retries**: Number of consecutive failures before marking unhealthy
- **start_period**: Grace period during startup (failures don't count toward retries)

## Verification

After applying these fixes, all services should show `(healthy)` status:

```bash
docker-compose ps
```

Expected output:
```
NAME                         STATUS
influencenet-elasticsearch   Up X minutes (healthy)
influencenet-keycloak        Up X minutes (healthy)
influencenet-mongodb         Up X minutes (healthy)
influencenet-neo4j           Up X minutes (healthy)
influencenet-postgres        Up X minutes (healthy)
influencenet-redis           Up X minutes (healthy)
...
```

## Troubleshooting

If a service still shows unhealthy:

1. **Check logs**:
   ```bash
   docker logs [container-name]
   ```

2. **Test health check manually**:
   ```bash
   docker exec [container-name] [health-check-command]
   ```

3. **Inspect health status**:
   ```bash
   docker inspect [container-name] | grep -A 20 Health
   ```

4. **Increase start_period** if service needs more startup time

## Notes

- Health checks are important for `depends_on` with `condition: service_healthy`
- Services like Kibana wait for Elasticsearch to be healthy before starting
- Keycloak waits for PostgreSQL to be healthy before starting
- Start periods prevent false negatives during initialization
