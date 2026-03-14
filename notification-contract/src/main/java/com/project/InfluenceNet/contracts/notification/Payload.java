package com.project.InfluenceNet.contracts.notification;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kind"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PostReminderPayload.class, name = "POST_REMINDER"),
        @JsonSubTypes.Type(value = CollabPayload.class, name = "COLLAB")
})
public interface Payload {
}
