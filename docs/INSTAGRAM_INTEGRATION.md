These three steps are related but serve different purposes, and whether you must do all depends on your stage (development vs production).

Let’s break them down clearly 👇

✅ Step 1 — Generate Access Tokens

Purpose: Allow your app to access your Instagram account data via API.

You do this first.

This step is for testing API access in development.

You add yourself as Instagram Tester

You authenticate via Facebook OAuth

Meta gives you a short-lived token → long-lived token

Now you can call IG Graph API

Without this step, you cannot pull posts, insights, followers, etc.

✅ Required for development
✅ Required for production
🎯 Gives you access to your IG data

✅ Step 2 — Configure Webhooks

Purpose: Receive real-time events from Instagram, like:

New post published

New comments

Mentions

Messaging updates (if enabled)

This step is only needed if your app needs live event notifications.

You can still pull data via API without webhooks.

✅ Needed if you want real-time updates
❌ Can skip initially if you're just fetching data manually

But since your platform aims at analytics, you’ll eventually need webhooks.

✅ Step 3 — Set up Instagram Business Login

Purpose: Let other users connect their IG account to your platform.

This is the OAuth flow UI:

Button: “Connect Instagram Account”

Redirect to Facebook login

User approves permissions

You store tokens → fetch their data

This is mandatory for a production SaaS / platform.

For now, in development, you can skip this until you finish #1 & #2.

✅ Required for production
❌ Optional during early dev if only using your test account

# Instagram Webhook Setup Guide

## Problem: "The callback URL or verify token couldn't be validated"

This error occurs when Instagram cannot reach your webhook endpoint or the verification fails.

## **Solution Steps**

### **1. Make Your Webhook Publicly Accessible**

Your webhook must be accessible from the internet. You have several options:

#### **Option A: Use ngrok (Recommended for Development)**

1. **Download ngrok**: https://ngrok.com/download
2. **Install and authenticate**:
   ```bash
   ngrok config add-authtoken YOUR_AUTH_TOKEN
   ```
3. **Start ngrok tunnel**:
   ```bash
   ngrok http 8080
   ```
4. **Copy the HTTPS URL** (e.g., `https://abc123.ngrok.io`)

#### **Option B: Deploy to Cloud (Production)**

Deploy your app to:
- **Heroku**: Free tier available
- **AWS EC2/ECS**: More control
- **Google Cloud Run**: Serverless option
- **Azure App Service**: Microsoft cloud

#### **Option C: Use localhost.run (Quick Testing)**

```bash
ssh -R 80:localhost:8080 localhost.run
```

### **2. Configure Your Application**

#### **A. Set Environment Variables**

Create a `.env` file or set system environment variables:

```bash
# Generate a secure random token (20+ characters)
INSTAGRAM_WEBHOOK_VERIFY_TOKEN=your_super_secure_random_token_here_12345

# Get from Facebook Developer Dashboard → Settings → Basic
INSTAGRAM_APP_SECRET=your_instagram_app_secret_here
```

#### **B. Update application.properties**

The properties are already configured to read from environment variables:
```properties
instagram.webhook.verify-token=${INSTAGRAM_WEBHOOK_VERIFY_TOKEN:my_secure_verify_token_12345}
instagram.app.secret=${INSTAGRAM_APP_SECRET:}
```

### **3. Start Your Application**

```bash
# Windows
set INSTAGRAM_WEBHOOK_VERIFY_TOKEN=your_token_here
set INSTAGRAM_APP_SECRET=your_secret_here
./gradlew bootRun

# Linux/Mac
export INSTAGRAM_WEBHOOK_VERIFY_TOKEN=your_token_here
export INSTAGRAM_APP_SECRET=your_secret_here
./gradlew bootRun
```

### **4. Test Your Webhook Locally**

Before configuring in Facebook, test your endpoint:

```bash
# Test GET (verification)
curl "http://localhost:8080/webhook/instagram?hub.mode=subscribe&hub.verify_token=your_token_here&hub.challenge=test123"

# Should return: test123
```

### **5. Configure in Facebook Developer Dashboard**

1. **Go to**: https://developers.facebook.com/apps
2. **Select your app** → **Instagram** → **Configuration**
3. **Add Webhook**:
   - **Callback URL**: `https://your-ngrok-url.ngrok.io/webhook/instagram`
   - **Verify Token**: Same as `INSTAGRAM_WEBHOOK_VERIFY_TOKEN`
   - **Fields**: Select what you want to subscribe to:
     - ✅ `comments` - New comments on media
     - ✅ `mentions` - When someone mentions you
     - ✅ `media` - New media posted
     - ✅ `story_insights` - Story metrics

4. **Click "Verify and Save"**

### **6. Common Issues & Solutions**

#### **Issue: "Callback URL couldn't be validated"**

**Causes:**
- Webhook endpoint not publicly accessible
- Wrong verify token
- Application not running
- Firewall blocking requests

**Solutions:**
```bash
# 1. Check if your app is running
curl http://localhost:8080/actuator/health

# 2. Check if ngrok is running
curl https://your-ngrok-url.ngrok.io/webhook/instagram?hub.mode=subscribe&hub.verify_token=your_token&hub.challenge=test

# 3. Check application logs for errors
tail -f logs/application.log
```

#### **Issue: "Connection timeout"**

**Solution:**
- Ensure your app responds within 5 seconds
- Check if firewall/antivirus is blocking incoming connections
- Verify ngrok tunnel is active

#### **Issue: "Invalid signature"**

**Solution:**
- Verify `INSTAGRAM_APP_SECRET` is correct
- Check it matches the "App Secret" in Facebook Developer Dashboard → Settings → Basic

### **7. Verify Webhook is Working**

#### **Check Logs**

After successful setup, you should see:
```
INFO  - Webhook verification request received - mode: subscribe, token: ***
INFO  - Webhook verified successfully
```

#### **Test with Real Events**

1. Post a new photo/video on your connected Instagram account
2. Check your application logs for:
   ```
   INFO  - Webhook update received
   INFO  - Processing webhook: {...}
   ```

### **8. Production Deployment Checklist**

- [ ] Use HTTPS (required by Instagram)
- [ ] Store secrets in environment variables or secret manager
- [ ] Implement proper error handling
- [ ] Add webhook event processing logic
- [ ] Set up monitoring and alerts
- [ ] Configure rate limiting
- [ ] Add request logging
- [ ] Implement retry mechanism for failed processing

### **9. Webhook Payload Examples**

#### **New Media Posted**
```json
{
  "object": "instagram",
  "entry": [{
    "id": "instagram-user-id",
    "time": 1569262486134,
    "changes": [{
      "field": "media",
      "value": {
        "media_id": "media-id"
      }
    }]
  }]
}
```

#### **New Comment**
```json
{
  "object": "instagram",
  "entry": [{
    "id": "instagram-user-id",
    "time": 1569262486134,
    "changes": [{
      "field": "comments",
      "value": {
        "media_id": "media-id",
        "id": "comment-id",
        "text": "Great post!"
      }
    }]
  }]
}
```

### **10. Next Steps**

After webhook is configured:

1. **Implement webhook processing logic** in `handleWebhook()` method
2. **Store webhook events** in database or message queue
3. **Process events asynchronously** to avoid timeouts
4. **Fetch additional data** using Instagram Graph API
5. **Update analytics** based on webhook events

### **11. Useful Commands**

```bash
# Generate secure random token
openssl rand -base64 32

# Test webhook endpoint
curl -X GET "https://your-domain.com/webhook/instagram?hub.mode=subscribe&hub.verify_token=YOUR_TOKEN&hub.challenge=CHALLENGE_STRING"

# Monitor logs in real-time
tail -f logs/spring.log | grep "webhook"

# Check ngrok connections
curl http://localhost:4040/api/tunnels
```

### **12. Security Best Practices**

1. **Always verify signatures** on POST requests
2. **Use HTTPS** in production
3. **Rotate verify tokens** periodically
4. **Rate limit** webhook endpoint
5. **Log all webhook events** for audit
6. **Validate payload structure** before processing
7. **Use environment variables** for secrets
8. **Implement IP whitelisting** if possible

### **13. Troubleshooting Checklist**

- [ ] Application is running on correct port (8080)
- [ ] ngrok tunnel is active and pointing to port 8080
- [ ] Verify token matches in both code and Facebook dashboard
- [ ] Webhook URL is HTTPS (ngrok provides this)
- [ ] No firewall blocking incoming connections
- [ ] Application logs show verification request
- [ ] GET endpoint returns challenge string
- [ ] App mode is set to "Live" in Facebook dashboard

## **Support Resources**

- **Instagram Webhooks Documentation**: https://developers.facebook.com/docs/instagram-api/webhooks
- **ngrok Documentation**: https://ngrok.com/docs
- **Facebook Developer Community**: https://developers.facebook.com/community/

## **Contact**

If you continue to have issues:
1. Check application logs: `logs/spring.log`
2. Check ngrok dashboard: `http://localhost:4040`
3. Verify Facebook App settings
4. Test endpoint manually with curl
