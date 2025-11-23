# Influencer Module

This module handles influencer profile management and social media account integration.

## Features

- **Influencer Profile Management**: Create, read, update, and delete influencer profiles
- **Social Account Integration**: Link and manage multiple social media accounts (Instagram, YouTube, TikTok, Twitter)
- **Follower Tracking**: Automatic aggregation of follower counts across platforms
- **OAuth Token Management**: Secure storage of access and refresh tokens

## Architecture

### Entities

#### InfluencerProfile
- **id**: UUID (Primary Key)
- **email**: Unique email address
- **username**: Unique username
- **bio**: Profile biography
- **profilePicUrl**: Profile picture URL
- **niche**: Content niche/category
- **location**: Geographic location
- **timezone**: User timezone
- **followerCountTotal**: Aggregated follower count across all platforms
- **avgEngagementRate**: Average engagement rate
- **isActive**: Account status
- **socialAccounts**: One-to-many relationship with SocialAccount
- **createdAt**: Creation timestamp
- **updatedAt**: Last update timestamp

#### SocialAccount
- **id**: UUID (Primary Key)
- **influencer**: Many-to-one relationship with InfluencerProfile
- **platform**: Social media platform (instagram, youtube, tiktok, twitter)
- **platformUserId**: Platform-specific user ID
- **username**: Platform username
- **accessTokenRef**: Reference to encrypted access token
- **refreshTokenRef**: Reference to encrypted refresh token
- **tokenExpiresAt**: Token expiration timestamp
- **isActive**: Account status
- **followerCount**: Platform-specific follower count
- **connectedAt**: Connection timestamp
- **lastSyncedAt**: Last synchronization timestamp
- **updatedAt**: Last update timestamp

### Repositories

- **InfluencerProfileRepository**: JPA repository for influencer profiles
- **SocialAccountRepository**: JPA repository for social accounts

### Services

- **InfluencerProfileService**: Business logic for profile and social account management

### Controllers

- **InfluencerProfileController**: REST API endpoints

## API Endpoints

### Influencer Profile Endpoints

#### Create Profile
```http
POST /api/influencer/profiles
Content-Type: application/json

{
  "email": "influencer@example.com",
  "username": "influencer123",
  "bio": "Fitness enthusiast and lifestyle blogger",
  "profilePicUrl": "https://example.com/profile.jpg",
  "niche": "fitness",
  "location": "Los Angeles, CA",
  "timezone": "America/Los_Angeles"
}
```

#### Get Profile by ID
```http
GET /api/influencer/profiles/{id}
```

#### Get Profile by Username
```http
GET /api/influencer/profiles/username/{username}
```

#### Get All Profiles
```http
GET /api/influencer/profiles
```

#### Update Profile
```http
PUT /api/influencer/profiles/{id}
Content-Type: application/json

{
  "email": "influencer@example.com",
  "username": "influencer123",
  "bio": "Updated bio",
  "profilePicUrl": "https://example.com/new-profile.jpg",
  "niche": "fitness",
  "location": "Los Angeles, CA",
  "timezone": "America/Los_Angeles"
}
```

#### Delete Profile
```http
DELETE /api/influencer/profiles/{id}
```

### Social Account Endpoints

#### Add Social Account
```http
POST /api/influencer/profiles/{influencerId}/social-accounts
Content-Type: application/json

{
  "platform": "instagram",
  "platformUserId": "12345678",
  "username": "influencer_insta",
  "accessTokenRef": "vault://tokens/instagram/access",
  "refreshTokenRef": "vault://tokens/instagram/refresh",
  "tokenExpiresAt": "2025-12-31T23:59:59",
  "followerCount": 50000
}
```

#### Get All Social Accounts
```http
GET /api/influencer/profiles/{influencerId}/social-accounts
```

#### Get Social Account by Platform
```http
GET /api/influencer/profiles/{influencerId}/social-accounts/{platform}
```

#### Remove Social Account
```http
DELETE /api/influencer/profiles/{influencerId}/social-accounts/{platform}
```

#### Update Social Account Sync
```http
PATCH /api/influencer/profiles/{influencerId}/social-accounts/{platform}/sync
Content-Type: application/json

{
  "followerCount": 52000
}
```

## Error Handling

The module uses custom exceptions for better error handling:

- **InfluencerNotFoundException**: Thrown when an influencer profile is not found (404)
- **SocialAccountNotFoundException**: Thrown when a social account is not found (404)
- **DuplicateResourceException**: Thrown when attempting to create duplicate resources (409)

## Validation

Request DTOs include validation annotations:

- **Email**: Must be valid email format
- **Username**: 3-100 characters, must be unique
- **Platform**: Must be one of: instagram, youtube, tiktok, twitter
- **Bio**: Maximum 1000 characters
- **Niche**: Maximum 100 characters
- **Location**: Maximum 200 characters
- **Timezone**: Maximum 50 characters

## Database Schema

The module uses PostgreSQL with Flyway migrations:

- **V1__initial_schema.sql**: Initial schema with basic tables
- **V2__update_influencer_profile_schema.sql**: Adds email, username, and engagement fields

## Future Enhancements

1. **OAuth Integration**: Implement full OAuth2 flow for social platforms
2. **Real-time Sync**: Scheduled jobs to sync follower counts and engagement metrics
3. **Analytics Integration**: Connect with analytics service for detailed insights
4. **Search & Filtering**: Advanced search capabilities by niche, location, follower count
5. **Profile Verification**: Email verification and identity verification
6. **Privacy Settings**: Granular privacy controls for profile visibility
