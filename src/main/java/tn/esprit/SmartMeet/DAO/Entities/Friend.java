package tn.esprit.SmartMeet.DAO.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "friends")
public class Friend {
    @Id
    private String id;
    private String userId;
    private String friendId;
    private LocalDateTime since;

    public Friend() {}

    public Friend(String userId, String friendId, LocalDateTime since) {
        this.userId = userId;
        this.friendId = friendId;
        this.since = since;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public LocalDateTime getSince() {
        return since;
    }

    public void setSince(LocalDateTime since) {
        this.since = since;
    }
// Getters & setters
}

