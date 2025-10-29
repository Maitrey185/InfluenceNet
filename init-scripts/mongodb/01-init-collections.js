// Switch to influencenet database
db = db.getSiblingDB('influencenet');

// Create collections with validation
db.createCollection('raw_posts', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['influencer_id', 'platform', 'platform_post_id', 'fetched_at'],
      properties: {
        influencer_id: {
          bsonType: 'string',
          description: 'UUID of the influencer'
        },
        platform: {
          bsonType: 'string',
          enum: ['instagram', 'youtube', 'tiktok', 'twitter'],
          description: 'Social media platform'
        },
        platform_post_id: {
          bsonType: 'string',
          description: 'Post ID from the platform'
        },
        raw_payload: {
          bsonType: 'object',
          description: 'Full API response'
        },
        fetched_at: {
          bsonType: 'date',
          description: 'When the post was fetched'
        }
      }
    }
  }
});

db.createCollection('raw_insights', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['post_id', 'platform', 'fetched_at'],
      properties: {
        post_id: {
          bsonType: 'string',
          description: 'Reference to post'
        },
        platform: {
          bsonType: 'string',
          enum: ['instagram', 'youtube', 'tiktok', 'twitter']
        },
        fetched_at: {
          bsonType: 'date'
        }
      }
    }
  }
});

db.createCollection('enriched_posts');

// Create indexes
db.raw_posts.createIndex({ 'influencer_id': 1, 'platform': 1, 'fetched_at': -1 });
db.raw_posts.createIndex({ 'platform_post_id': 1 }, { unique: true });
db.raw_insights.createIndex({ 'post_id': 1, 'fetched_at': -1 });
db.enriched_posts.createIndex({ 'post_id': 1 });
db.enriched_posts.createIndex({ 'influencer_id': 1, 'enriched_at': -1 });

print('MongoDB collections and indexes created successfully');
