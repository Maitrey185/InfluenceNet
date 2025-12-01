package com.project.InfluenceNet.socialConnector.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "raw_posts")
public class RawPosts {

    @Id
    private String id;

    private UUID influencer_id;
    private Platforms platform;
    private Object raw_payload;
    private LocalDate fetched_at;
    private PostType post_type;
    private String caption;
    private String media_url;
    private String permalink;
    private LocalDateTime timestamp;

}
