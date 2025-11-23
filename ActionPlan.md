I'll examine your current authentication setup and identify where OAuth integration fits.



# OAuth Integration Points

## Current State

Your authentication is **currently disabled** (line 28-29 in [application.properties](cci:7://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/resources/application.properties:0:0-0:0)). You have **two OAuth scenarios** to implement:

## 1. **User Authentication OAuth (Keycloak)** - COMMENTED OUT
**Purpose**: Authenticate users (influencers/brands) to access your platform
- **Status**: Infrastructure ready but disabled
- **Config**: Lines 181-204 in [application.properties](cci:7://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/resources/application.properties:0:0-0:0)
- **Implementation**: [SecurityConfig.java](cci:7://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/java/com/project/InfluenceNet/config/SecurityConfig.java:0:0-0:0) and [KeycloakConfig.java](cci:7://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/java/com/project/InfluenceNet/config/KeycloakConfig.java:0:0-0:0) (all commented)

## 2. **Social Media OAuth (Instagram/YouTube/TikTok/Twitter)** - MISSING
**Purpose**: Connect influencers' social accounts to fetch data
- **Status**: Placeholders only (lines 234-238)
- **Where it happens**: When influencers link their social accounts to [SocialAccount](cci:2://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/java/com/project/InfluenceNet/influencer/entity/SocialAccount.java:11:0-49:1) entity
- **Required for**: Fetching follower counts, engagement rates, posts

---

## When to Implement OAuth

### **Immediate Priority: Social Media OAuth**
You need this **now** because:

1. **[SocialAccount](cci:2://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/java/com/project/InfluenceNet/influencer/entity/SocialAccount.java:11:0-49:1) entity has OAuth fields** (lines 33-40):
    - `accessToken`
    - `refreshToken`
    - `tokenExpiresAt`

2. **These are empty without OAuth flow**

3. **Your platform cannot function** without social data

### **Implementation Flow:**

```
Influencer logs in → Dashboard → "Connect Instagram" button
  ↓
OAuth Flow:
  1. Redirect to Instagram OAuth authorization
  2. User grants permissions
  3. Instagram redirects back with authorization code
  4. Exchange code for access_token + refresh_token
  5. Store in SocialAccount entity
  6. Fetch initial follower_count and engagement_rate
```

### **Later Priority: Keycloak OAuth**
Implement when you're ready to enable authentication (currently using security disabled mode for development).

---

## Next Steps

**You should implement Social Media OAuth when:**
1. ✅ Entities are set up (done - you just added [User](cci:2://file:///C:/Users/maitr/IdeaProjects/InfluenceNet/InfluenceNet/src/main/java/com/project/InfluenceNet/influencer/entity/User.java:11:0-27:1) relationship)
2. ✅ Database schema exists (done)
3. ⏳ **Next**: Create OAuth controllers for each platform
4. ⏳ Register apps with Instagram/YouTube/TikTok/Twitter
5. ⏳ Add OAuth client libraries (e.g., `spring-boot-starter-oauth2-client`)

**Typical endpoint structure:**
- `GET /api/social/instagram/connect` - Initiates OAuth
- `GET /api/social/instagram/callback` - Handles OAuth callback
- `POST /api/social/instagram/disconnect` - Removes connection