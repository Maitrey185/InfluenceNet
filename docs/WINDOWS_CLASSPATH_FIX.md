# Windows Classpath Error Fix

## Error: "CreateProcess error=206, The filename or extension is too long"

This error occurs on Windows when the command line exceeds the maximum length (8191 characters), commonly caused by long classpaths in Java/Gradle projects.

## **Solutions (Try in Order)**

### **Solution 1: Use IntelliJ IDEA's Built-in Fix (Easiest)**

1. **Open IntelliJ IDEA**
2. **Go to**: `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Gradle`
3. **Set "Build and run using"**: `IntelliJ IDEA` (instead of Gradle)
4. **Set "Run tests using"**: `IntelliJ IDEA`
5. **Click**: `Apply` → `OK`
6. **Run your application** using the green play button

### **Solution 2: Use Gradle Daemon (Already Configured)**

The `gradle.properties` file has been created with:
```properties
org.gradle.daemon=true
org.gradle.configureondemand=true
```

**To use it:**
```powershell
# Clean and rebuild
./gradlew clean build

# Run application
./gradlew bootRun
```

### **Solution 3: Shorten Project Path**

Move your project to a shorter path:

**Current path:**
```
C:\Users\maitr\IdeaProjects\InfluenceNet\InfluenceNet
```

**Suggested shorter path:**
```
C:\IN\
```

**Steps:**
1. Close IntelliJ IDEA
2. Move folder: `C:\Users\maitr\IdeaProjects\InfluenceNet\InfluenceNet` → `C:\IN\`
3. Open IntelliJ IDEA
4. Open project from new location

### **Solution 4: Use JAR Classpath (Spring Boot)**

Spring Boot creates an executable JAR with manifest classpath:

```powershell
# Build the JAR
./gradlew clean bootJar

# Run the JAR directly
java -jar build/libs/InfluenceNet-0.0.1-SNAPSHOT.jar
```

### **Solution 5: Run from IDE with Shortened Classpath**

**In IntelliJ IDEA:**

1. **Go to**: `Run` → `Edit Configurations`
2. **Select your Spring Boot configuration**
3. **Under "Shorten command line"**: Select `JAR manifest` or `classpath file`
4. **Click**: `Apply` → `OK`
5. **Run the application**

### **Solution 6: Use Docker (Recommended)**

Your project has docker-compose configured. Run everything in containers:

```powershell
# Start all services (databases, kafka, etc.)
docker-compose up -d

# Check if services are running
docker-compose ps

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

**Note:** Kafka UI is on port 8080, which conflicts with your Spring Boot app. Change it:

Edit `docker-compose.yml` line 143:
```yaml
ports:
  - "8090:8080"  # Changed from 8080:8080
```

Then run your Spring Boot app normally on port 8080.

## **Recommended Solution for Your Project**

Use **Solution 1** (IntelliJ IDEA settings) + **Solution 6** (Docker for services):

### **Step-by-Step:**

1. **Start infrastructure services:**
   ```powershell
   docker-compose up -d postgres mongodb redis
   ```

2. **Configure IntelliJ IDEA:**
   - `File` → `Settings` → `Build Tools` → `Gradle`
   - Set "Build and run using": `IntelliJ IDEA`
   - Apply changes

3. **Run your application:**
   - Click the green play button in IntelliJ
   - Or use: `./gradlew bootRun` (should work now with gradle.properties)

## **Verify the Fix**

After applying the solution, verify:

```powershell
# Check if app starts
curl http://localhost:8080/actuator/health

# Check database connection
curl http://localhost:8080/actuator/health/db

# Test your webhook endpoint
curl "http://localhost:8080/webhook/instagram?hub.mode=subscribe&hub.verify_token=test&hub.challenge=test123"
```

## **Additional Tips**

### **Clean Gradle Cache**
```powershell
./gradlew clean
./gradlew --stop
./gradlew build
```

### **Check Java Version**
```powershell
java -version
# Should be Java 17
```

### **Enable Gradle Daemon**
```powershell
# Check if daemon is running
./gradlew --status

# Start daemon
./gradlew --daemon
```

## **If Nothing Works**

### **Nuclear Option: Fresh Start**

1. **Stop all Gradle daemons:**
   ```powershell
   ./gradlew --stop
   ```

2. **Delete Gradle cache:**
   ```powershell
   Remove-Item -Recurse -Force $env:USERPROFILE\.gradle\caches
   ```

3. **Rebuild:**
   ```powershell
   ./gradlew clean build --no-daemon
   ```

4. **Run:**
   ```powershell
   java -jar build\libs\InfluenceNet-0.0.1-SNAPSHOT.jar
   ```

## **Summary**

✅ **Created `gradle.properties`** with daemon settings  
✅ **Updated `build.gradle.kts`** with classpath fixes  
✅ **Docker services** available on different ports  
✅ **IntelliJ IDEA** can use JAR manifest classpath  

**Recommended approach:** Use IntelliJ IDEA's built-in classpath shortening (Solution 1) - it's the simplest and most reliable.
