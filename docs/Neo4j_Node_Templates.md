CREATE (:Influencer {
id: UUID,
name: String,
primaryNiche: String,
primaryPlatform: String,
followerCount: Int,
engagementRate: Float,
growthRate30d: Float,        // % follower growth
growthTrend: String,
platformAgeDays: Int,
joinedAt: Date,
postsPerWeek: Float,
consistencyScore: Float
})

(:Niche {
id: String,          // "fitness"
name: String,        // "Fitness"
level: Int           // optional
})

(:Post {
id: String,
platform: String,
postedAt: DateTime,
engagementRate: Float
})



