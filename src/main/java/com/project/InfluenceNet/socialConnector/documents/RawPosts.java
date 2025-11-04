package com.project.InfluenceNet.socialConnector.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "raw_posts")
public class RawPosts {

    @Id
    private ObjectId id;

    private UUID influencer_id;
    private Enum<Platforms> platform;
    private String platform_post_id;
    private Object raw_payload;
    private Date fetched_at;
    private Enum<PostType> post_type;
    private String caption;
    private String media_url;
    private String permalink;
    private Date timestamp;

}
