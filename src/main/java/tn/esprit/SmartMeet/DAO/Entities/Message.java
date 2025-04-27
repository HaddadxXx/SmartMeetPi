package tn.esprit.SmartMeet.DAO.Entities;


import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "messages")
public class Message {
    @Id
    private String id;


    private String content;
    private String senderId;
    private String conversationId;

    private Instant timestamp;
    private boolean isReply;




    private boolean isRead;
    public Message() {}
    public Message(String conversationId, String senderId, String content) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = Instant.now();
    }
    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }







}

