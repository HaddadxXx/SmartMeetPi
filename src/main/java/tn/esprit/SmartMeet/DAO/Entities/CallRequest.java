package tn.esprit.SmartMeet.DAO.Entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "call_requests")
public class CallRequest {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String from; // ID de l'utilisateur qui envoie la demande
    private String to;   // ID de l'utilisateur qui reçoit la demande
    private String status; // "pending" ou "accepted"

    public CallRequest(String from, String to, String status) {
        this.from = from;
        this.to = to;
        this.status = status;
    }
    public CallRequest() {

    }
}
