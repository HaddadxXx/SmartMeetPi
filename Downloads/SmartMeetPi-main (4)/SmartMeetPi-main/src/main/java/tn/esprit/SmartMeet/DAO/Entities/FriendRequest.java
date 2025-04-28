package tn.esprit.SmartMeet.DAO.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "friend_requests")
public class FriendRequest {
    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private Status status;
    private LocalDateTime createdAt;

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
// Constructeurs, Getters, Setters

    public FriendRequest(String id, String senderId, String receiverId, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = createdAt;
    }
    public FriendRequest() {}

}

